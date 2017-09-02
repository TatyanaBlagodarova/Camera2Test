package com.camera2.test.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;

import com.camera2.test.R;
import com.camera2.test.utils.HtmlCreator;
import com.camera2.test.utils.IntentUtils;

/**
 * Created by Tatyana Blagodarova on 5/25/17.
 */
public class FullModeFragment extends Fragment {
    private WebView mWebView;
    private String mResultHtml;

    public FullModeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fullmode, container, false);
        v.findViewById(R.id.send_to_dev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createReport();
            }
        });
        mWebView = (WebView) v.findViewById(R.id.webview_info);
        loadHtmlIntoWebView();
        return v;
    }

    private void createReport() {
        if (!mResultHtml.isEmpty()) {
            IntentUtils.startShareIntent(getActivity(), mResultHtml);
        } else {
            Toast.makeText(getActivity(), "Sorry, there is nothing to send.", Toast.LENGTH_LONG).show();
        }
    }

    private void loadHtmlIntoWebView() {
        mResultHtml = HtmlCreator.createHtml(getActivity());
        mWebView.loadDataWithBaseURL("", HtmlCreator.getColoredReadyHtml(mResultHtml), "text/html", "utf-8", "");
    }
}