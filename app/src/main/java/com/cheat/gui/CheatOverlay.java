package com.cheat.module;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.cheat.utils.Config;
import com.cheat.utils.RenderHook;
import com.cheat.utils.TeamFilter;

public class CheatOverlay extends SurfaceView implements SurfaceHolder.Callback {
    private final Config config;
    private final Paint linePaint, bonePaint, boxPaint, fovPaint;
    private Thread renderThread;
    private volatile boolean running = true;

    public CheatOverlay(Context context, Config cfg) {
        super(context);
        this.config = cfg;
        getHolder().addCallback(this);
        setZOrderOnTop(true);
        getHolder().setFormat(android.graphics.PixelFormat.TRANSLUCENT);

        linePaint = new Paint();
        linePaint.setColor(Color.parseColor("#FF3DE03D"));
        linePaint.setStrokeWidth(2f);
        linePaint.setStyle(Paint.Style.STROKE);

        bonePaint = new Paint();
        bonePaint.setColor(Color.parseColor("#FF00E5FF"));
        bonePaint.setStrokeWidth(2f);
        bonePaint.setStyle(Paint.Style.STROKE);

        boxPaint = new Paint();
        boxPaint.setColor(Color.WHITE);
        boxPaint.setStrokeWidth(1.5f);
        boxPaint.setStyle(Paint.Style.STROKE);

        fovPaint = new Paint();
        fovPaint.setColor(Color.parseColor("#88FFFFFF"));
        fovPaint.setStyle(Paint.Style.STROKE);
        fovPaint.setStrokeWidth(1f);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        renderThread = new Thread(() -> {
            while (running) {
                Canvas canvas = holder.lockCanvas();
                if (canvas != null) {
                    drawFrame(canvas);
                    holder.unlockCanvasAndPost(canvas);
                }
                try { Thread.sleep(16); } catch (InterruptedException ignored) {}
            }
        });
        renderThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        running = false;
        try { renderThread.join(); } catch (InterruptedException ignored) {}
    }

    private void drawFrame(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);

        int cx = canvas.getWidth() / 2;
        int cy = canvas.getHeight() / 2;

        if (config.isFovCircleEnabled()) {
            float radius = canvas.getWidth() * config.getFovSize();
            canvas.drawCircle(cx, cy, radius, fovPaint);
        }

        if (!config.isEspEnabled()) return;

        float[][] targets = RenderHook.getEntities();
        if (targets == null) return;

        int myTeam = RenderHook.getMyTeam();
        for (float[] bones : targets) {
            if (bones == null || bones.length < 5) continue;
            float headX = bones[0], headY = bones[1];
            float feetX = bones[2], feetY = bones[3];
            int tid = (int) bones[4];
            // Своих не рисуем (WH только по врагам)
            if (!TeamFilter.filterByTeamMarker(tid, myTeam)) continue;
            drawSkeleton(canvas, bones);
            drawLine(canvas, cx, canvas.getHeight(), headX, headY);
            float w = Math.max(40, (feetY - headY) * 0.35f);
            canvas.drawRect(feetX - w/2, headY, feetX + w/2, feetY, boxPaint);
        }
    }

    private void drawSkeleton(Canvas canvas, float[] bones) {
        float hx = bones[0], hy = bones[1];
        float fx = bones[2], fy = bones[3];
        float neckX = (hx + fx) / 2, neckY = hy + (fy - hy) * 0.18f;
        float shoulderX = (hx + fx) / 2 + 25, shoulderY = neckY + 10;
        float hipX = (hx + fx) / 2, hipY = hy + (fy - hy) * 0.55f;

        Path p = new Path();
        p.moveTo(hx, hy);
        p.lineTo(neckX, neckY);
        p.lineTo(shoulderX, shoulderY);
        canvas.drawPath(p, bonePaint);
    }

    private void drawLine(Canvas canvas, int x1, int y1, float x2, float y2) {
        canvas.drawLine(x1, y1, x2, y2, linePaint);
    }
}
