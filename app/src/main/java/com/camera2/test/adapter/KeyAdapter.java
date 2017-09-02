package com.camera2.test.adapter;

import android.content.Context;
import android.hardware.camera2.CameraCharacteristics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.camera2.test.R;

import java.util.List;


/**
 * Created by Tatyana Blagodarova on 5/25/17.
 */

public class KeyAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private List<CameraCharacteristics.Key<?>> objects;

    public KeyAdapter(Context context, List<CameraCharacteristics.Key<?>> objects) {
        super();
        this.objects = objects;
        this.inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        row = inflater.inflate(R.layout.row_key, parent, false);

        CameraCharacteristics.Key<?> key = objects.get(position);
        if (key != null) {
            TextView textView = (TextView) row.findViewById(R.id.text1);
            if (textView != null) {
                textView.setText(key.getName());
            }
        }
        return row;
    }
}
