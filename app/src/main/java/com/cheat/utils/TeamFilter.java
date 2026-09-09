package com.cheat.utils;

public class TeamFilter {
    // === RE: данные из data.unity3d (Standoff 2 / Axlebolt) ===
    // Класс игрока (Assembly-CSharp):    PlayerStats
    // Поле/эн метки команды:             Team  (тип Team)
    // Присвоение команды при матчмейке:  assign_t_team
    // Команды (внутренние): 0 = neutral/наблюдатель,
    //                       1 = Counter-Terrorist (CtSpawn),
    //                       2 = Terrorist        (TrSpawn)
    // Нашлась заметка "teamchangerestricted" - команду меняют только на спавне,
    // значит фильтровать по team-id на старте раунда безопасно.

    // Класс игрока - переименован встроенным обфускатором, но нити через полный путь:
    private static final String PLAYER_CLASS = "PlayerStats";
    private static final String TEAM_FIELD = "Team";
    private static final String ASSIGN_TEAM_METHOD = "assign_t_team";

    // Почти все Standoff-хуки считывают команду как int через Reflection.
    // В libx.so уже есть ESP по слотам сердечников: используем team id из него.
    public static native int getLocalTeamId();

    static {
        // libx.so уже прохукан в общий процесс модом;
        // сюда заводим только пересчёт, т.к. сам первый слой сделает loadLibrary.
        try { System.loadLibrary("x"); } catch (Throwable ignored) {}
    }

    // 0 = neutral/спектатор, 1 = CT, 2 = T
    public static boolean filterByTeamMarker(int teamId, int myTeamId) {
        return teamId != 0 && teamId != myTeamId;
    }

    // Проверка перед aim/trigger/WH: для каждого видимого PlayerStats
    public static boolean shouldTarget(float[] bones, int teamId, int myTeamId) {
        if (bones == null) return false;
        return filterByTeamMarker(teamId, myTeamId);
    }

    // Отображение ID команды в короткое имя (для оверлея)
    public static String teamName(int teamId) {
        switch (teamId) {
            case 1: return "CT";
            case 2: return "T";
            default: return "?";
        }
    }
}