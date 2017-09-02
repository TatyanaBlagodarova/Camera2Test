package com.camera2.test.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;
import android.widget.ScrollView;

/**
 * Created by Tatyana Blagodarova on 6/6/17.
 */

public class CustomScrollView extends ScrollView {
    public CustomScrollView(Context context) {
        super(context);
    }

    public CustomScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public CustomScrollView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        if (focused instanceof WebView)
            return;
        super.requestChildFocus(child, focused);
    }
}