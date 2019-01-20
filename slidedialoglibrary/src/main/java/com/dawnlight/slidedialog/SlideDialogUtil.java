package com.dawnlight.slidedialog;


import android.app.Activity;
import android.support.v4.app.Fragment;

public class SlideDialogUtil {

    public static boolean validTargetFragment(Fragment fragment) {

        return fragment != null && fragment.getTargetFragment() != null && fragment.getTargetFragment().isAdded();
    }

    public static boolean validFragment(Fragment fragment) {

        return fragment != null && fragment.isAdded();
    }

    public static boolean isEmptySafe(String s) {
        return s == null || s.isEmpty();
    }

    public static boolean validActivity(Activity activity) {
        return activity != null && !activity.isFinishing() && !activity.isDestroyed();
    }
}
