package com.camera2.test.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.camera2.test.R;
import com.camera2.test.activity.ResultsActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Tatyana Blagodarova on 5/16/17.
 */

public class IntentUtils {
    public static void startShareIntent(Context context, String html) {
        String deviceInfo = "";
        deviceInfo += "\n OS Version: " + System.getProperty("os.version") + "(" +
                android.os

                        .Build.VERSION.INCREMENTAL + ")";
        deviceInfo += "\n OS API Level: " + android.os.Build.VERSION.RELEASE + "(" +
                android.os
                        .Build.VERSION.SDK_INT + ")";
        deviceInfo += "\n Device: " + android.os.Build.DEVICE;
        deviceInfo += "\n Model (and Product): " + android.os.Build.MODEL + " (" +
                android.os.Build.PRODUCT + ")";
        File path = context.getExternalCacheDir();
        File filelocation = new File(path, android.os.Build.DEVICE + " device_info" +
                ".html");
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(filelocation);
            stream.write(html.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        //// TODO: 5/13/2017 prepare support mail for feedback
//                  intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"blagodarovatv@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.email_subject));
        intent.putExtra(Intent.EXTRA_TEXT, deviceInfo);
        Uri pathToFile = Uri.fromFile(filelocation);
        intent.putExtra(Intent.EXTRA_STREAM, pathToFile);
        ((Activity) context).startActivityForResult(Intent.createChooser(intent, "Send Email"), 1);
    }

    public static void startResultsActivity(Context context) {
        Intent intent = new Intent(context, ResultsActivity.class);
        context.startActivity(intent);
    }
}
