package com.skin.core.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Binder;
import android.os.Build;

import com.skin.core.R;

public class SkinThemeUtils {

    private static int[] APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS = {
            android.support.v7.appcompat.R.attr.colorPrimaryDark
    };

    private static int[] STATUSBAR_COLOR_ATTRS = {android.R.attr.statusBarColor, android.R.attr
            .navigationBarColor};

    private static int[] TYPEFACE_ATTRS = {R.attr.skinTypeface};

    public static int[] getResId(Context context, int[] attrs) {
        int[] resIds = new int[attrs.length];
        TypedArray a = context.obtainStyledAttributes(attrs);
        for (int i = 0; i < attrs.length; i++) {
            resIds[i] = a.getResourceId(i, 0);
        }
        a.recycle();
        return resIds;
    }

    public static void updateStatusBarColor(Activity activity) {
        //5.0以上才能修改
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        //获得 statusBarColor 与 nanavigationBarColor (状态栏颜色)
        //当与 colorPrimaryDark  不同时 以statusBarColor为准
        int[] statusBarColorResId = getResId(activity, STATUSBAR_COLOR_ATTRS);
        if(statusBarColorResId[0] != 0) {
            activity.getWindow().setStatusBarColor(SkinResources.getInstance().getColor(statusBarColorResId[0]));
        }else {
            int colorPrimaryDarkResId = getResId(activity, APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS)[0];
            if(colorPrimaryDarkResId != 0) {
                activity.getWindow().setStatusBarColor(colorPrimaryDarkResId);
            }
        }
        if (statusBarColorResId[1] != 0) {
            activity.getWindow().setNavigationBarColor(SkinResources.getInstance().getColor
                    (statusBarColorResId[1]));
        }
    }

    public static Typeface getSkinTypeface(Activity activity) {
        int skinTypefaceId = getResId(activity, TYPEFACE_ATTRS)[0];
        return SkinResources.getInstance().getTypeface(skinTypefaceId);
    }
}
