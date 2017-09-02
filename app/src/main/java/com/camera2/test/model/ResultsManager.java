package com.camera2.test.model;


import android.content.Context;
import android.hardware.camera2.CameraCharacteristics;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tatyana Blagodarova on 5/16/17.
 */

public class ResultsManager {
    private static ResultsManager mInstance = null;
    private String[] mCameraIdList;
    private Map<String, CameraCharacteristics> mCameraIdCharacteristic = new HashMap<>();
    private Map<String, String> mKeyDescriptionsMap = new HashMap<>();

    public static ResultsManager getInstance() {
        if (mInstance == null) {
            mInstance = new ResultsManager();
        }
        return mInstance;
    }

    public void init(Context context) {
        createKeyDescriptionsMap(context);
    }

    public String[] getCameraIdList() {
        return mCameraIdList;
    }

    public void setCameraIdList(String[] mCameraIdList) {
        this.mCameraIdList = mCameraIdList;
    }

    public Map<String, CameraCharacteristics> getCameraIdCharacteristic() {
        return mCameraIdCharacteristic;
    }

    public Map<String, String> getKeyDescriptionsMap() {
        return mKeyDescriptionsMap;
    }

    public void putCameraCharacteristics(String cameraID, CameraCharacteristics characteristics) {
        mCameraIdCharacteristic.put(cameraID, characteristics);
    }

    public CameraCharacteristics getCameraIdCharacteristic(String cameraID) {
        return mCameraIdCharacteristic.get(cameraID);
    }

    private void createKeyDescriptionsMap(Context context) {
        ObjectInputStream input = null;
        try {
            input = new ObjectInputStream(context.getAssets().open("commentsMap.ser"));
            mKeyDescriptionsMap = (HashMap) input.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
