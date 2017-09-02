package com.camera2.test.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.camera2.test.R;

/**
 * Created by Tatyana Blagodarova on 5/13/17.
 */

public class PermissionsHelper {
    public static final int REQUEST_VIDEO_PERMISSIONS = 1;

    public static final String[] getAppPermissions() {
        return new String[]{Manifest.permission.CAMERA};
    }

    public static boolean hasPermissionsGranted(final Activity context, String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private static boolean shouldShowRequestPermissionRationale(final Activity context, String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(context, permission)) {
                return true;
            }
        }
        return false;
    }

    public static void requestVideoPermissions(final Activity context) {
        if (shouldShowRequestPermissionRationale(context, PermissionsHelper.getAppPermissions())) {
            new AlertDialog.Builder(context)
                    .setMessage(context.getString(R.string.permission_request))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(context, PermissionsHelper
                                            .getAppPermissions(),
                                    REQUEST_VIDEO_PERMISSIONS);
                        }
                    })
                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    context.finish();
                                }
                            })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(context, PermissionsHelper.getAppPermissions(), REQUEST_VIDEO_PERMISSIONS);
        }
    }
}
