package com.cheat.utils;

public class RenderHook {
    // Каждый элемент: [headX, headY, feetX, feetY, teamId]
    private static float[][] entities;

    private static volatile int myTeam = 0;

    public static void setEntities(float[][] data, int localTeam) {
        entities = data;
        myTeam = localTeam;
    }

    public static float[][] getEntities() {
        return entities;
    }

    public static int getMyTeam() {
        return myTeam;
    }

    // --- Аимбат ---
    private static volatile boolean aimbotActive = false;
    private static volatile float targetScreenX = 0f, targetScreenY = 0f;
    private static volatile int targetTeam = 0;

    // Выбирает цель строго из вражеской команды (свои отфильтрованы)
    public static boolean acquireTarget(int screenW, int screenH, float fovNorm) {
        aimbotActive = false;
        if (entities == null || myTeam == 0) return false;

        float bestX = 0, bestY = 0;
        float bestDist = Float.MAX_VALUE;
        boolean found = false;
        int bestTeam = 0;
        float cx = screenW / 2f, cy = screenH / 2f;
        float fovR = screenW * fovNorm;

        for (float[] e : entities) {
            if (e == null || e.length < 5) continue;
            float hx = e[0], hy = e[1];
            int tid = (int) e[4];
            if (!TeamFilter.filterByTeamMarker(tid, myTeam)) continue; // СВОИХ НЕ ТРОГАЕМ

            float dx = hx - cx, dy = hy - cy;
            float dist = (float) Math.sqrt(dx * dx + dy * dy);
            if (dist <= fovR && dist < bestDist) {
                bestDist = dist;
                bestX = hx; bestY = hy;
                bestTeam = tid;
                found = true;
            }
        }
        if (found) {
            targetScreenX = bestX;
            targetScreenY = bestY;
            targetTeam = bestTeam;
            aimbotActive = true;
            return true;
        }
        return false;
    }

    public static boolean isTargetValid() {
        return aimbotActive;
    }

    public static float getTargetX() { return targetScreenX; }
    public static float getTargetY() { return targetScreenY; }
    public static int getTargetTeam() { return targetTeam; }

    public static void clearAimTarget() {
        aimbotActive = false;
    }

    // --- Триггер бот ---
    private static volatile boolean triggerFired = false;
    public static boolean shouldTrigger(float screenW, float screenH) {
        if (!aimbotActive) return false;
        // стреляем только когда прицел (центр экрана) внутри цели и это враг
        float cx = screenW / 2f, cy = screenH / 2f;
        float dx = targetScreenX - cx, dy = targetScreenY - cy;
        float dist = (float) Math.sqrt(dx * dx + dy * dy);
        if (dist < screenH * 0.03f) { // жёсткий радиус головы
            boolean s = triggerFired;
            triggerFired = false;
            return s;
        }
        return false;
    }

    public static void setTriggerFired() {
        triggerFired = true;
    }

    // --- Стрельба сквозь стены (отдельный тумблер, не смешивать с аимом) ---
    private static volatile boolean wallbang = false;
    public static void setWallbang(boolean b) { wallbang = b; }
    public static boolean isWallbang() { return wallbang; }
}