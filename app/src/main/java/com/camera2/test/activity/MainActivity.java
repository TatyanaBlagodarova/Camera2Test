package com.camera2.test.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraCharacteristics;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.camera2.test.R;
import com.camera2.test.dialog.TOSDialog;
import com.camera2.test.interfaces.ResultListener;
import com.camera2.test.model.ResultsManager;
import com.camera2.test.task.CameraAnalyzeTask;
import com.camera2.test.utils.IntentUtils;
import com.camera2.test.utils.PermissionsHelper;

import static com.camera2.test.dialog.TOSDialog.ACCEPTED_STATUS;

/**
 * Created by Tatyana Blagodarova on 5/25/17.
 */
public class MainActivity extends AppCompatActivity implements ResultListener {
    private CameraAnalyzeTask mCamera2AnalyzeTask;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!PermissionsHelper.hasPermissionsGranted(this, PermissionsHelper.getAppPermissions())) {
            PermissionsHelper.requestVideoPermissions(this);
        }

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        findViewById(R.id.button_analyze).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                mCamera2AnalyzeTask = new CameraAnalyzeTask(MainActivity.this, MainActivity.this);
                mCamera2AnalyzeTask.execute();
            }
        });
        SharedPreferences sPref = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        Boolean statusAccepted = sPref.getBoolean(ACCEPTED_STATUS, false);

        if (!statusAccepted) {
            TOSDialog cdd = new TOSDialog(this);
            cdd.show();
        }
    }

    @Override
    public void onCameraIdsSetup(String[] cameraIdList) {
        ResultsManager.getInstance().setCameraIdList(cameraIdList);
    }

    @Override
    public void onCameraCharacteristicsGet(String cameraID, CameraCharacteristics characteristics) {
        ResultsManager.getInstance().putCameraCharacteristics(cameraID, characteristics);
    }

    @Override
    public void onAnalyzeFinished() {
        mProgressBar.setVisibility(View.GONE);
        IntentUtils.startResultsActivity(this);
        finish();
    }

    @Override
    public void onAnalyzeStarted() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PermissionsHelper.REQUEST_VIDEO_PERMISSIONS) {
            if (grantResults.length == PermissionsHelper.getAppPermissions().length) {
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        new AlertDialog.Builder(this)
                                .setMessage(getString(R.string.permission_request))
                                .setPositiveButton(android.R.string.ok, new DialogInterface
                                        .OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                })
                                .create().show();
                        break;
                    }
                }
            } else {
                new AlertDialog.Builder(this)
                        .setMessage(getString(R.string.permission_request))
                        .setPositiveButton(android.R.string.ok, new DialogInterface
                                .OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .create().show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCamera2AnalyzeTask != null) {
            mCamera2AnalyzeTask.cancel(true);
        }
    }
}
