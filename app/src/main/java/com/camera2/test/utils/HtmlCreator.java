package com.camera2.test.utils;

import android.content.Context;
import android.hardware.camera2.CameraCharacteristics;

import com.camera2.test.model.ResultsManager;
import com.samskivert.mustache.Mustache;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Tatyana Blagodarova on 5/16/17.
 */

public class HtmlCreator {

    public static String getReadyHtml(String result) {
        String s = "<html><head></head><body>" + result + "</body></html>";
        return s;
    }

    public static String getColoredReadyHtml(String result) {
        String s = "<html><head><style>\n" +
                "p.small {\n" +
                "    line-height: 1;\n" +
                "}\n" +
                "</style></head><body bgcolor=\"#f9ffe6\">" + result + "</body></html>";
        return s;
    }

    public static String createHtml(Context context) {
        StringBuilder sb = new StringBuilder();
        for (String cameraID : ResultsManager.getInstance().getCameraIdList()) {

            CameraCharacteristics characteristics = ResultsManager.getInstance().getCameraIdCharacteristic(cameraID);
            Map<String, Object> scopes = new HashMap<>();
            scopes.put("cameraId", String.format(Locale.US, "%s (%s)", cameraID, KeyValueConvertor.getCameraName(cameraID)));

            List<CameraCharacteristics.Key<?>> keys = characteristics.getKeys();

            Map<String, List<String>> mapSamePackages = CameraUtility.createSamePackageKeysMap(cameraID);

            List<Map<String, Object>> characteristicPackagesList = new ArrayList<>();

            Iterator it = mapSamePackages.entrySet().iterator();
            while (it.hasNext()) {

                Map.Entry pair = (Map.Entry) it.next();
                System.out.println(pair.getKey() + " = " + pair.getValue());
                String partsPackageName = (String) pair.getKey();

                List<String> partsList = (List<String>) pair.getValue();
                List<Map<String, String>> characteristicListToTemplate = new ArrayList<>();

                for (int i = 0; i < partsList.size(); i++) {
                    CameraCharacteristics.Key<?> currentKey = null;

                    for (CameraCharacteristics.Key<?> key : keys) {
                        if (key.getName().equals(partsPackageName + "." + partsList.get(i))) {
                            currentKey = key;
                            break;
                        }
                    }

                    if (currentKey != null) {
                        Map<String, String> m = getMapForCharacteristicKey(characteristics, currentKey, partsList.get(i));
                        if (m != null) {
                            characteristicListToTemplate.add(m);
                        }
                    }
                }

                Map<String, Object> c1 = new HashMap<>();
                c1.put("title", partsPackageName);
                c1.put("characteristicList", characteristicListToTemplate);
                characteristicPackagesList.add(c1);
            }

            scopes.put("characteristicPackage", characteristicPackagesList);
            try {
                sb.append(Mustache.compiler().compile(new InputStreamReader(context.getAssets().open("camera.mustache"))).execute(scopes));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    private static Map<String, String> getMapForCharacteristicKey(
            CameraCharacteristics cc,
            CameraCharacteristics.Key<?> k, String keyNameToShow) {
        String strValue = KeyValueConvertor.getValueName(k, cc.get(k));

        if (strValue.contains("@")) return null;

        Map<String, String> m = new HashMap<>();
        m.put("key", "- " + keyNameToShow);
        m.put("value", strValue);
        return m;
    }
}
