package com.camera2.test.fragment;

import android.hardware.camera2.CameraCharacteristics;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.camera2.test.R;
import com.camera2.test.adapter.KeyAdapter;
import com.camera2.test.model.ResultsManager;
import com.camera2.test.utils.HtmlCreator;
import com.camera2.test.utils.KeyValueConvertor;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import timber.log.Timber;

/**
 * Created by Tatyana Blagodarova on 5/25/17.
 */
public class KeyModeFragment extends Fragment {
    public static final String SAVED_KEY_CAMERA_ID = "SAVED_KEY_CAMERA_ID";
    public static final String SAVED_KEY_CAMERA_KEY = "SAVED_KEY_CAMERA_KEY";

    private ProgressBar mProgressBar;
    private Spinner mCameraSpinner;
    private Spinner mKeysSpinner;
    private TextView mTextKeyValue;
    private WebView mWebview;
    private int mSelectedKeyPosition;
    private int mSelectedCameraPosition;
    private ScrollView mScrollView;

    public KeyModeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_keymode, container, false);
        if (savedInstanceState != null) {
            mSelectedCameraPosition = savedInstanceState.getInt(SAVED_KEY_CAMERA_ID, 0);
            mSelectedKeyPosition = savedInstanceState.getInt(SAVED_KEY_CAMERA_KEY, 0);
        }
        initUIElements(v);
        updateUIWithResults();
        Timber.v("onCreateView");
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_KEY_CAMERA_ID, mCameraSpinner.getSelectedItemPosition());
        outState.putInt(SAVED_KEY_CAMERA_KEY, mKeysSpinner.getSelectedItemPosition());
    }

    private void initUIElements(View v) {
        mCameraSpinner = (Spinner) v.findViewById(R.id.camera_spinner);
        mKeysSpinner = (Spinner) v.findViewById(R.id.key_spinner);
        mTextKeyValue = (TextView) v.findViewById(R.id.value_view);
        mWebview = (WebView) v.findViewById(R.id.value_webview);
        mScrollView = (ScrollView) v.findViewById(R.id.main_scrollview);
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.setWebViewClient(new MyWebViewClient());
        mCameraSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setKeysToTheSpinner();
                mSelectedCameraPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mKeysSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showKeyValue();
                mSelectedKeyPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void showKeyValue() {
        int selectedCameraPosition = mCameraSpinner.getSelectedItemPosition();
        int selectedKeyPosition = mKeysSpinner.getSelectedItemPosition();

        CameraCharacteristics charac = ResultsManager.getInstance().getCameraIdCharacteristic().get(ResultsManager.getInstance().getCameraIdList()[selectedCameraPosition]);
        List<CameraCharacteristics.Key<?>> allKeys = charac.getKeys();

        Object toShow = charac.get(allKeys.get(selectedKeyPosition));
        String readableString = KeyValueConvertor.getValueName(allKeys.get(selectedKeyPosition), toShow);
        mTextKeyValue.setText(Html.fromHtml(readableString));

        Map<String, String> map = ResultsManager.getInstance().getKeyDescriptionsMap();
        String htmlKeyDescription = map.get(allKeys.get(selectedKeyPosition).getName());
        // this is needed for clear cached data
        mWebview.clearHistory();
        mWebview.loadUrl("about:blank");

        if (htmlKeyDescription == null) {
            mWebview.loadData(HtmlCreator.getColoredReadyHtml(getString(R.string.no_doc_found)), "text/html; charset=utf-8", "UTF-8");
        } else {
            mWebview.loadData(HtmlCreator.getColoredReadyHtml(htmlKeyDescription), "text/html; charset=utf-8", "UTF-8");
        }
    }


    private void updateUIWithResults() {
        String[] camIds = ResultsManager.getInstance().getCameraIdList();
        String[] camNames = new String[2];
        for (int i = 0; i < camIds.length; i++) {
            camNames[i] = String.format(Locale.US, "%s (%s)", camIds[i], KeyValueConvertor.getCameraName(camIds[i]));
        }
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.row_key, camNames);

        mCameraSpinner.setSelection(mSelectedCameraPosition);
        mCameraSpinner.setAdapter(adapter);
    }

    private void setKeysToTheSpinner() {
        int selectedItemPosition = mCameraSpinner.getSelectedItemPosition();
        CameraCharacteristics characteristic = ResultsManager.getInstance().getCameraIdCharacteristic().get(ResultsManager.getInstance().getCameraIdList()[selectedItemPosition]);

        BaseAdapter adapter = new KeyAdapter(getActivity(), characteristic.getKeys());
        mKeysSpinner.setAdapter(adapter);
        mKeysSpinner.setSelection(mSelectedKeyPosition);
    }

    private class MyWebViewClient extends WebViewClient {
        private boolean clearHistory = false;

        /**
         * Use this instead of WebView.clearHistory().
         */
        public void clearHistory() {
            clearHistory = true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (clearHistory) {
                clearHistory = false;
                mWebview.clearHistory();
            }
            super.onPageFinished(view, url);
        }
    }
}
