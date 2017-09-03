package com.camera2.test.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;

import com.camera2.test.R;
import com.camera2.test.utils.HtmlCreator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Tatyana Blagodarova on 8/29/17.
 */

public class TOSDialog extends Dialog implements android.view.View.OnClickListener {
    public interface onAcceptedPPListener {
        void onPPAccepted();
    }

    public static final String ACCEPTED_STATUS = "ACCEPTED_STATUS";
    private final SharedPreferences sPref;
    public Context context;
    public Button yes, no;
    public WebView mWebView;
    private onAcceptedPPListener mListener;

    public TOSDialog(Context c, onAcceptedPPListener listener) {
        super(c, R.style.DialogTheme);
        this.context = c;
        this.mListener = listener;
        sPref = ((Activity) context).getSharedPreferences(context.getString(R.string.app_name), MODE_PRIVATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tos_dialog);

        yes = (Button) findViewById(R.id.btn_yes);
        no = (Button) findViewById(R.id.btn_no);
        mWebView = (WebView) findViewById(R.id.webview);

        yes.setOnClickListener(this);
        no.setOnClickListener(this);

        StringBuilder total = new StringBuilder();
        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(context.getAssets().open("privacy_policy.html")));
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        mWebView.loadDataWithBaseURL("", HtmlCreator.getColoredReadyHtml(total.toString()), "text/html", "utf-8", "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                SharedPreferences.Editor ed = sPref.edit();
                ed.putBoolean(ACCEPTED_STATUS, true);
                ed.commit();
                dismiss();
                if (mListener != null) {
                    mListener.onPPAccepted();
                }
                break;
            case R.id.btn_no:
                SharedPreferences.Editor ed1 = sPref.edit();
                ed1.putBoolean(ACCEPTED_STATUS, false);
                ed1.commit();
                ((Activity) context).finish();
                break;
            default:
                break;
        }
        dismiss();
    }
}
