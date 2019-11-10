package com.example.controller;

import android.net.Uri;
import android.provider.BaseColumns;

public final class Alarm {

    public static class Columns implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.parse("content://com.urbandroid.sleep.alarmclock/alarm");

        public static final String HOUR = "hour";
        public static final String MINUTES = "minutes";
        public static final String DAYS_OF_WEEK = "daysofweek";
        public static final String ALARM_TIME = "alarmtime";
        public static final String SUSPEND_TIME = "suspendtime";
        public static final String NON_DEEPSLEEP_WAKEUP_WINDOWN = "ndswakeupwindow";
        public static final String ENABLED = "enabled";
        public static final String VIBRATE = "vibrate";
        public static final String MESSAGE = "message";
        public static final String ALERT = "alert";
        public static final String DEFAULT_SORT_ORDER = HOUR + ", " + MINUTES + " ASC";

        public static final int ALARM_ID_INDEX = 0;
        public static final int ALARM_HOUR_INDEX = 1;
        public static final int ALARM_MINUTES_INDEX = 2;
        public static final int ALARM_DAYS_OF_WEEK_INDEX = 3;
        public static final int ALARM_TIME_INDEX = 4;
        public static final int ALARM_ENABLED_INDEX = 5;
        public static final int ALARM_VIBRATE_INDEX = 6;
        public static final int ALARM_MESSAGE_INDEX = 7;
        public static final int ALARM_ALERT_INDEX = 8;
        public static final int ALARM_SUSPEND_TIME_INDEX = 9;
        public static final int ALARM_NON_DEEPSLEEP_WAKEUP_WINDOW_INDEX = 10;
    }
}