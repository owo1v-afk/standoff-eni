package com.cheat.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.View;

public class ChamsHook {
    public static void applyPlayerTint(View target, int color) {
        if (target == null) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            target.setBackgroundTintList(ColorStateList.valueOf(color));
        } else {
            target.setBackgroundDrawable(new ColorDrawable(color));
        }
    }

    public static void tintChams(Context ctx) {
        NativeHook.attach(ctx.getPackageName());
    }
}
