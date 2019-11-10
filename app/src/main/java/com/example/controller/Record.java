package com.example.controller;

import android.net.Uri;
import android.provider.BaseColumns;

public class Record {
    public static final String AUTHORITY = "com.urbandroid.sleep.history";
    public static final String RECORDS_TABLE = "records";

    public static class Records implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + RECORDS_TABLE);
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/com.urbandroid.sleep.history";

        public static final String RECORD_ID = "_id";
        public static final String START_TIME = "startTime";
        public static final String LATEST_TO_TIME = "latestToTime";
        public static final String TO_TIME = "toTime";
        public static final String FRAMERATE = "framerate";
        public static final String RATING = "rating";
        public static final String COMMENT = "comment";
        public static final String RECORD_DATA = "recordData";
        public static final String TIMEZONE = "timezone";
        public static final String LEN_ADJUST = "lenAdjust";
        public static final String QUALITY = "quality";
        public static final String SNORE = "snore";
        public static final String CYCLES = "cycles";
        public static final String EVENT_LABELS = "eventLabels";
        public static final String EVENTS = "events";
        public static final String RECORD_FULL_DATA = "recordFullData";
        public static final String RECORD_NOISE_DATA = "recordNoiseData";
        public static final String NOISE_LEVEL = "noiseLevel";
        public static final String FINISHED = "finished";
        public static final String GEO = "geo";
        public static final String LENGTH = "length";
    }
}
