package com.cheat.utils;

public class NativeHook {
    static {
        System.loadLibrary("eni");
    }

    public static native boolean attach(String packageName);
    public native static boolean simulateTap(int x, int y);
    public static native void injectView();
    public static native void setFovRadius(float radius);
}
