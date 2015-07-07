package com.ray.passwordmanager.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Ray on 15/7/6.
 */
public class IconTintHelper {
    private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};
    private static final int[] DISABLED_STATE_SET = {-android.R.attr.state_enabled};
    private static final int[] EMPTY_STATE_SET = new int[]{};

    public static void setIconTintList(ImageView iconView, ColorStateList csl){
        Drawable icon = iconView.getDrawable();
        if (icon != null) {
            icon = DrawableCompat.wrap(icon);
//            icon = icon.mutate();
//            icon.setBounds(0, 0, mIconSize, mIconSize);
            DrawableCompat.setTintList(icon, csl);
        }
        DrawableCompat.setTintList(icon, csl);
        iconView.setImageDrawable(icon);
    }

    public static ColorStateList createDefaultColorStateList(Context context) {
        return createDefaultColorStateList(context, android.R.attr.textColorSecondary);
    }

    public static ColorStateList createDefaultColorStateList(Context context, int baseColorThemeAttr) {
        TypedValue value = new TypedValue();
        if (context.getTheme().resolveAttribute(baseColorThemeAttr, value, true)) {
            return null;
        }
        ColorStateList baseColor = context.getResources().getColorStateList(value.resourceId);
        if (context.getTheme().resolveAttribute(android.support.design.R.attr.colorPrimary, value, true)) {
            return null;
        }
        int colorPrimary = value.data;
        assert baseColor != null;
        int defaultColor = baseColor.getDefaultColor();
        return new ColorStateList(new int[][]{
                DISABLED_STATE_SET,
                CHECKED_STATE_SET,
                EMPTY_STATE_SET
        }, new int[]{
                baseColor.getColorForState(DISABLED_STATE_SET, defaultColor),
                colorPrimary,
                defaultColor
        });
    }
}
