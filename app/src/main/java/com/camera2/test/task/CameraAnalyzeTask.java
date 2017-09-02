package com.camera2.test.task;

import android.content.Context;
import android.os.AsyncTask;

import com.camera2.test.interfaces.ResultListener;
import com.camera2.test.model.ResultsManager;
import com.camera2.test.utils.CameraUtility;

/**
 * Created by Tatyana Blagodarova on 5/16/17.
 */

public class CameraAnalyzeTask extends AsyncTask<Boolean, Void, Boolean> {
    private final Context mContext;
    private final ResultListener mResultListener;

    public CameraAnalyzeTask(Context context, ResultListener listener) {
        this.mContext = context;
        this.mResultListener = listener;
    }

    @Override
    protected Boolean doInBackground(Boolean... params) {
        ResultsManager.getInstance().init(mContext);
        CameraUtility.checkCamera2Abilities(mContext, mResultListener);
        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        mResultListener.onAnalyzeFinished();
    }

    @Override
    protected void onPreExecute() {
        mResultListener.onAnalyzeStarted();
    }
}
