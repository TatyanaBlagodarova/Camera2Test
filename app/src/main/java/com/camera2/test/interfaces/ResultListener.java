package com.camera2.test.interfaces;

import android.hardware.camera2.CameraCharacteristics;

/**
 * Created by Tatyana Blagodarova on 5/16/17.
 */

public interface ResultListener {
    void onCameraIdsSetup(String[] cameraIdList);

    void onCameraCharacteristicsGet(String cameraID, CameraCharacteristics characteristics);

    void onAnalyzeFinished();

    void onAnalyzeStarted();
}
