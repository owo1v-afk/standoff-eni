package com.cheat.module;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.WindowManager;
import com.cheat.gui.CheatOverlay;
import com.cheat.gui.MenuOverlay;
import com.cheat.utils.Config;

public class CheatService extends Service {
    private CheatOverlay overlay;
    private MenuOverlay menu;
    private WindowManager wm;
    private Config config;
    private WindowManager.LayoutParams menuParams;

    @Override
    public void onCreate() {
        super.onCreate();
        config = Config.getInstance(this);
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        addOverlay();

        addMenu();
    }

    private void addOverlay() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        );
        params.gravity = Gravity.TOP | Gravity.START;

        overlay = new CheatOverlay(this, config);
        wm.addView(overlay, params);
    }

    private void addMenu() {
        menuParams = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        );
        menuParams.gravity = Gravity.TOP | Gravity.START;
        menuParams.x = 24;
        menuParams.y = 120;

        menu = new MenuOverlay(this, config, this::stopSelf, new MenuOverlay.WindowMover() {
            @Override
            public void moveWindow(int deltaX, int deltaY) {
                if (menuParams == null) return;
                menuParams.x = Math.max(0, menuParams.x + deltaX);
                menuParams.y = Math.max(0, menuParams.y + deltaY);
                try { wm.updateViewLayout(menu, menuParams); } catch (Exception ignored) {}
            }

            @Override
            public void close() {
                stopSelf();
            }
        });

        wm.addView(menu, menuParams);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (overlay != null) { try { wm.removeView(overlay); } catch (Exception ignored) {} overlay = null; }
        if (menu != null) { try { wm.removeView(menu); } catch (Exception ignored) {} menu = null; }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}