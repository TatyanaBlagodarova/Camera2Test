package com.camera2.test.utils;

import android.content.Context;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.support.annotation.NonNull;

import com.camera2.test.interfaces.ResultListener;
import com.camera2.test.model.ResultsManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by Tatyana Blagodarova on 5/16/17.
 */

public class CameraUtility {
    public static void checkCamera2Abilities(Context mContext, @NonNull ResultListener listener) {
        CameraManager manager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
        try {
            String[] cameraIdList = manager.getCameraIdList();
            listener.onCameraIdsSetup(cameraIdList);
            for (String cameraID : cameraIdList) {
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraID);
                listener.onCameraCharacteristicsGet(cameraID, characteristics);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<String, List<String>> createSamePackageKeysMap(String cameraID) {
        Map<String, List<String>> resultMap = new HashMap<>();

        CameraCharacteristics characteristics = ResultsManager.getInstance().getCameraIdCharacteristic(cameraID);
        List<CameraCharacteristics.Key<?>> keys = characteristics.getKeys();

        for (CameraCharacteristics.Key<?> key : keys) {

            StringTokenizer st2 = new StringTokenizer(key.getName(), ".");
            ArrayList<String> result = new ArrayList<>(st2.countTokens());
            while (st2.hasMoreElements()) {
                result.add(st2.nextToken());
            }

            if (result.size() > 1) {
                String packageNameWithoutLastPart = createPackageFrom(result);
                String part = result.get(result.size() - 1);

                if (resultMap.get(packageNameWithoutLastPart) != null) {
                    List<String> packageParts = resultMap.get(packageNameWithoutLastPart);
                    packageParts.add(part);
                    resultMap.put(packageNameWithoutLastPart, packageParts);
                } else {
                    ArrayList<String> packageParts = new ArrayList<String>();
                    packageParts.add(part);
                    resultMap.put(packageNameWithoutLastPart, packageParts);
                }
            }
        }
        return resultMap;
    }

    private static String createPackageFrom(ArrayList<String> result) {
        String res = "";
        for (int i = 0; i < result.size() - 1; i++) {
            res += result.get(i) + (i == result.size() - 2 ? "" : ".");
        }
        return res;
    }
}