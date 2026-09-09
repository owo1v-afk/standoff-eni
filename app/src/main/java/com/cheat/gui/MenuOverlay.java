package com.cheat.gui;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.widget.SwitchCompat;
import com.cheat.utils.Config;

public class MenuOverlay extends LinearLayout {
    private final Config config;
    private final Runnable onClose;
    private final WindowMover windowMover;

    private float downRawX, downRawY, downParentX, downParentY;
    private boolean dragged = false;

    public interface WindowMover {
        void moveWindow(int deltaX, int deltaY);
        void close();
    }

    public MenuOverlay(Context context, Config cfg, Runnable close, WindowMover mover) {
        super(context);
        this.config = cfg;
        this.onClose = close;
        this.windowMover = mover;

        setOrientation(VERTICAL);
        setPadding(dp(10), dp(10), dp(10), dp(10));
        setBackgroundColor(0xCC121212);
        setElevation(dp(8));

        LinearLayout wrap = wrapTitleBar();
        addView(wrap);

        addSwitch("Aimbot", config.isAimbotEnabled(), v -> config.setAimbotEnabled(v), "sw_aimbot");
        addSwitch("Trigger Bot", config.isTriggerEnabled(), v -> config.setTriggerEnabled(v), "sw_trigger");
        addSwitch("ESP", config.isEspEnabled(), v -> config.setEspEnabled(v), "sw_esp");
        addSwitch("Chams", config.isChamsEnabled(), v -> config.setChamsEnabled(v), "sw_chams");
        addSwitch("FOV Circle", config.isFovCircleEnabled(), v -> config.setFovCircleEnabled(v), "sw_fov");

        TextView speedLabel = new TextView(context);
        speedLabel.setText("Aim speed: " + config.getAimSpeed());
        speedLabel.setTextColor(0xFFFFFFFF);
        addView(speedLabel);

        SeekBar seek = new SeekBar(context);
        seek.setMax(100);
        seek.setProgress((int) (config.getAimSpeed() * 100));
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar s, int p, boolean f) {
                float v = p / 100f;
                config.setAimSpeed(v);
                speedLabel.setText("Aim speed: " + v);
            }
            @Override public void onStartTrackingTouch(SeekBar s) {}
            @Override public void onStopTrackingTouch(SeekBar s) {}
        });
        addView(seek, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, dp(40)));
    }

    private LinearLayout wrapTitleBar() {
        LinearLayout bar = new LinearLayout(getContext());
        bar.setOrientation(HORIZONTAL);
        bar.setGravity(android.view.Gravity.CENTER);
        bar.setBackgroundColor(0xFF1B5E20);
        bar.setPadding(dp(8), dp(4), dp(8), dp(4));

        TextView title = new TextView(getContext());
        title.setText("== ENI MENU ==");
        title.setTextColor(0xFFFFFFFF);
        title.setTextSize(14);
        title.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f));
        bar.addView(title);

        View close = new View(getContext());
        close.setLayoutParams(new LayoutParams(dp(20), dp(20)));
        close.setOnClickListener(v -> {
            if (onClose != null) onClose.run();
        });
        bar.addView(close);
        return bar;
    }

    private void addSwitch(String label, boolean state, java.util.function.Consumer<Boolean> cb, String tag) {
        SwitchCompat sw = new SwitchCompat(getContext());
        sw.setText(label);
        sw.setTextColor(0xFFFFFFFF);
        sw.setTag(tag);
        sw.setChecked(state);
        sw.setOnCheckedChangeListener((b, checked) -> cb.accept(checked));
        addView(sw);
    }

    private int dp(int v) {
        return Math.round(getResources().getDisplayMetrics().density * v);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downRawX = event.getRawX();
                downRawY = event.getRawY();
                downParentX = getX();
                downParentY = getY();
                dragged = false;
                return true;
            case MotionEvent.ACTION_MOVE: {
                float dx = event.getRawX() - downRawX;
                float dy = event.getRawY() - downRawY;
                if (Math.abs(dx) > dp(4) || Math.abs(dy) > dp(4)) {
                    dragged = true;
                }
                if (dragged && windowMover != null) {
                    windowMover.moveWindow((int) dx, (int) dy);
                }
                return true;
            }
            case MotionEvent.ACTION_UP:
                return dragged;
        }
        return super.onTouchEvent(event);
    }

    public boolean wasDragged() {
        return dragged;
    }
}