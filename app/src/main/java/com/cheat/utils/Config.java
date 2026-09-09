package com.cheat.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Config {
    private static Config instance;
    private final SharedPreferences prefs;

    private Config(Context ctx) {
        prefs = ctx.getSharedPreferences("cheat_cfg", Context.MODE_PRIVATE);
    }

    public static synchronized Config getInstance(Context ctx) {
        if (instance == null) instance = new Config(ctx.getApplicationContext());
        return instance;
    }

    public float getAimSpeed() { return prefs.getFloat("aim_speed", 0.6f); }
    public void setAimSpeed(float v) { prefs.edit().putFloat("aim_speed", v).apply(); }

    public boolean isAimbotEnabled() { return prefs.getBoolean("aimbot", true); }
    public void setAimbotEnabled(boolean b) { prefs.edit().putBoolean("aimbot", b).apply(); }

    public boolean isTriggerEnabled() { return prefs.getBoolean("trigger", true); }
    public void setTriggerEnabled(boolean b) { prefs.edit().putBoolean("trigger", b).apply(); }

    public boolean isEspEnabled() { return prefs.getBoolean("esp", true); }
    public void setEspEnabled(boolean b) { prefs.edit().putBoolean("esp", b).apply(); }

    public boolean isChamsEnabled() { return prefs.getBoolean("chams", true); }
    public void setChamsEnabled(boolean b) { prefs.edit().putBoolean("chams", b).apply(); }

    public boolean isFovCircleEnabled() { return prefs.getBoolean("fov_circle", true); }
    public void setFovCircleEnabled(boolean b) { prefs.edit().putBoolean("fov_circle", b).apply(); }

    public float getFovSize() { return prefs.getFloat("fov_size", 0.12f); }
    public void setFovSize(float v) { prefs.edit().putFloat("fov_size", v).apply(); }
}
