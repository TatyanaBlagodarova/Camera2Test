package com.camera2.test.fragment;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.camera2.test.R;
import com.camera2.test.dialog.TOSDialog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Tatyana Blagodarova on 5/25/17.
 */

public class InfoFragment extends Fragment {
    public InfoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_info, container, false);
        TextView versionTv = (TextView) rootView.findViewById(R.id.version_tv);
        WebView aboutThisApp = (WebView) rootView.findViewById(R.id.webview_about);
        rootView.findViewById(R.id.button_policy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TOSDialog cdd = new TOSDialog(getActivity());
                cdd.show();
            }
        });

        StringBuilder total = new StringBuilder();
        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(getActivity().getAssets().open("about.html")));
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line).append('\n');
            }

            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            String version = pInfo.versionName;
            int verCode = pInfo.versionCode;
            versionTv.setText("v" + version + " (" + verCode + ")");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        aboutThisApp.loadDataWithBaseURL("", total.toString(), "text/html", "utf-8", "");
        return rootView;
    }
}
