package com.ray.passwordmanager.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Ray on 15/7/3.
 */
public class ToastUtil {
    public static void showS(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

    public static void showL(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
    }

    public static void showS(Context context, CharSequence text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void showL(Context context, CharSequence text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}
