/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.keli.hfbus.alarm;

import java.text.DateFormatSymbols;
import java.util.Calendar;

import com.keli.hfbus.R;

import android.content.Context;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;
import android.util.Log;

public final class Alarm implements Parcelable {

    public static final Parcelable.Creator<Alarm> CREATOR
            = new Parcelable.Creator<Alarm>() {
                public Alarm createFromParcel(Parcel p) {
                    return new Alarm(p);
                }

                public Alarm[] newArray(int size) {
                    return new Alarm[size];
                }
            };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel p, int flags) {
        p.writeInt(id);
        p.writeInt(enabled ? 1 : 0);
        p.writeInt(hour);
        p.writeInt(minutes);
        p.writeInt(daysOfWeek.getCoded());
        p.writeLong(time);
        p.writeInt(vibrate ? 1 : 0);
        p.writeString(label);
        p.writeParcelable(alert, flags);
        p.writeInt(silent ? 1 : 0);
        p.writeString(stationDistance);
        p.writeString(arriveStation);
        p.writeString(startSation);
        p.writeString(endStation);
        p.writeString(orientation);
        p.writeString(linear);
        p.writeInt(flag ? 1 : 0);
        p.writeString(orderId);
        p.writeString(stationId);
        p.writeString(linearId);
    }
    public static class Columns implements BaseColumns {
        /**
         * The content:// 濞戞捇缂氱换鏍ㄧ▔椤忓洢锟介悗瑙勭煯缁犵喐绋夐幒褜浜滈崣鈩冪椤愩倖鐣盪rl
         */
        public static final Uri CONTENT_URI =
                Uri.parse("content://com.keli.hfbus/alarm");

        /**
         * Hour in 24-hour localtime 0 - 23.
         * <P>Type: INTEGER</P>
         */
        public static final String HOUR = "hour";

        /**
         * Minutes in localtime 0 - 59
         * <P>Type: INTEGER</P>
         */
        public static final String MINUTES= "minutes";
        
        public static final String DAYS_OF_WEEK = "daysofweek";

        /**
         * Alarm time in UTC milliseconds from the epoch.
         * <P>Type: INTEGER</P>
         */
        public static final String ALARM_TIME = "alarmtime";
 

        /**
         * True if alarm is active
         * <P>Type: BOOLEAN</P>
         */
        public static final String ENABLED = "enabled";

        /**
         * True if alarm should vibrate
         * <P>Type: BOOLEAN</P>
         */
        public static final String VIBRATE = "vibrate";

        /**
         * Message to show when alarm triggers
         * Note: not currently used
         * <P>Type: STRING</P>
         */
        public static final String MESSAGE = "message";

        /**
         * Audio alert to play when alarm triggers
         * <P>Type: STRING</P>
         */
        public static final String ALERT = "alert";
        public static final String stationDistance="stationDistance";
        public static final String arriveStation="arriveStation";
        public static final String startSation="startSation";
        public static final String endStation="endStation";
        public static final String orientation="orientation";
        public static final String linear="linear";
        public static final String flag="flag";
        public static final String orderId="orderId";//服务器中的预约id
        public static final String stationId="stationId";//站点id
        public static final String linearId="linearId";
       // public static final String label="0";
        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER =
                HOUR + ", " + MINUTES + " ASC";

        // Used when filtering enabled alarms.
        public static final String WHERE_ENABLED = ENABLED + "=1";

        static final String[] ALARM_QUERY_COLUMNS = {
            _ID, HOUR, MINUTES,DAYS_OF_WEEK, ALARM_TIME,ENABLED, VIBRATE, MESSAGE, ALERT,stationDistance,arriveStation,startSation,endStation,orientation,linear,flag,orderId,stationId,linearId};

        /**
         * These save calls to cursor.getColumnIndexOrThrow()
         * THEY MUST BE KEPT IN SYNC WITH ABOVE QUERY COLUMNS
         */
        public static final int ALARM_ID_INDEX = 0;
        public static final int ALARM_HOUR_INDEX = 1;
        public static final int ALARM_MINUTES_INDEX = 2;
        public static final int ALARM_DAYS_OF_WEEK_INDEX = 3;
        public static final int ALARM_TIME_INDEX = 4;
        public static final int ALARM_ENABLED_INDEX = 5;
        public static final int ALARM_VIBRATE_INDEX = 6;
        public static final int ALARM_MESSAGE_INDEX = 7;
        public static final int ALARM_ALERT_INDEX = 8;
        public static final int ALARM_stationDistance_INDEX = 9;
        public static final int ALARM_arriveStation_INDEX = 10;
        public static final int ALARM_startSation_INDEX = 11;
        public static final int ALARM_endStation_INDEX = 12;
        public static final int ALARM_orientation_INDEX = 13;
        public static final int ALARM_LINEAR_INDEX = 14;
        public static final int ALARM_FLAG_INDEX =15;
        public static final int ALARM_ORDER_ID_INDEX =16;
        public static final int ALARM_STATION_ID_INDEX=17;
        public static final int ALARM_LINEAR_ID_INDEX=18;
    }
    //////////////////////////////
    // End 婵絽绻嬬粩鎾礆濡わ拷鏆板☉鏂款樈缁劑鏁撻敓锟� //////////////////////////////

    // 閻庣數鎳撶花鏌ユ儍閸曨偄褰嗛柛蹇氫含濞堟垵袙韫囧海顏遍柛鎺擃殶濞堟垿寮伴悩鑼
    public int        id;
    public boolean    enabled;
    public int        hour;
    public int        minutes;
    public DaysOfWeek daysOfWeek;
    public long       time;
    public boolean    vibrate;
    public String     label;//闂傚倸缍婇幐鎾诲冀閸モ晩鍔�
    public Uri        alert;
    public boolean    silent;
    public  String stationDistance;
    public String arriveStation;
    public String startSation;
    public  String endStation;
    public  String orientation;
    public  String linear;
    public boolean flag;//閺嶅洩顔囬弰顖炴闁界喐妲搁崥锕�嚒缂佸繐鎼锋惔锟�    
    public String orderId;//服务器中的预约id
    public String stationId;
    public String linearId;
    public Alarm(Cursor c) {
        id = c.getInt(Columns.ALARM_ID_INDEX);
        enabled = c.getInt(Columns.ALARM_ENABLED_INDEX) == 1;
        hour = c.getInt(Columns.ALARM_HOUR_INDEX);
        minutes = c.getInt(Columns.ALARM_MINUTES_INDEX);
        daysOfWeek = new DaysOfWeek(c.getInt(Columns.ALARM_DAYS_OF_WEEK_INDEX));
        time = c.getLong(Columns.ALARM_TIME_INDEX);
        vibrate = c.getInt(Columns.ALARM_VIBRATE_INDEX) == 1;
        flag = c.getInt(Columns.ALARM_FLAG_INDEX) == 1;
        label = c.getString(Columns.ALARM_MESSAGE_INDEX);
        stationDistance=c.getString(Columns.ALARM_stationDistance_INDEX);
        arriveStation=c.getString(Columns.ALARM_arriveStation_INDEX);
        startSation=c.getString(Columns.ALARM_startSation_INDEX);
        endStation=c.getString(Columns.ALARM_endStation_INDEX);
        orientation=c.getString(Columns.ALARM_orientation_INDEX);
        linear=c.getString(Columns.ALARM_LINEAR_INDEX);
        orderId=c.getString(Columns.ALARM_ORDER_ID_INDEX);
        stationId=c.getString(Columns.ALARM_STATION_ID_INDEX);
        linearId=c.getString(Columns.ALARM_LINEAR_ID_INDEX);
        String alertString = c.getString(Columns.ALARM_ALERT_INDEX);
        if (Alarms.ALARM_ALERT_SILENT.equals(alertString)) {
            if (true) {
                Log.v("wangxianming", "Alarm is marked as silent");
            }
            silent = true;
        } else {
            if (alertString != null && alertString.length() != 0) {
                alert = Uri.parse(alertString);
            }

            // If the database alert is null or it failed to parse, use the
            // default alert.
            if (alert == null) {
                alert = RingtoneManager.getDefaultUri(
                        RingtoneManager.TYPE_ALARM);
            }
        }
    }

    public Alarm(Parcel p) {
        id = p.readInt();
        enabled = p.readInt() == 1;
        hour = p.readInt();
        minutes = p.readInt();
        daysOfWeek = new DaysOfWeek(p.readInt());
        time = p.readLong();
        vibrate = p.readInt() == 1;
        label = p.readString();
        alert = (Uri) p.readParcelable(null);
        silent = p.readInt() == 1;
        stationDistance=p.readString();
        arriveStation=p.readString();
        startSation=p.readString();
        endStation=p.readString();
        orientation=p.readString();
        linear=p.readString();
        flag=p.readInt() == 1; 
        orderId=p.readString();
        stationId=p.readString();
        linearId=p.readString();
    }

    public Alarm() {
        id = -1;
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        hour = c.get(Calendar.HOUR_OF_DAY);
        minutes = c.get(Calendar.MINUTE);
        vibrate = true;
        daysOfWeek = new DaysOfWeek(0);
        alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        flag=false;
    }

    public String getLabelOrDefault(Context context) {
        return label;
    }

    /*
     * Days of week code as a single int.
     * 0x00: no day
     * 0x01: Monday
     * 0x02: Tuesday
     * 0x04: Wednesday
     * 0x08: Thursday
     * 0x10: Friday
     * 0x20: Saturday
     * 0x40: Sunday
     */
    public static final class DaysOfWeek {

        private static int[] DAY_MAP = new int[] {
            Calendar.MONDAY,
            Calendar.TUESDAY,
            Calendar.WEDNESDAY,
            Calendar.THURSDAY,
            Calendar.FRIDAY,
            Calendar.SATURDAY,
            Calendar.SUNDAY,
        };

        // Bitmask of all repeating days
        private int mDays;

        public DaysOfWeek(int days) {
            mDays = days;
        }

        public String toString(Context context, boolean showNever) {
            StringBuilder ret = new StringBuilder();

            // no days
            if (mDays == 0) {
                return showNever ?
                        context.getText(R.string.never).toString() : "";
            }

            // every day
            if (mDays == 0x7f) {
                return context.getText(R.string.every_day).toString();
            }

            // count selected days
            int dayCount = 0, days = mDays;
            while (days > 0) {
                if ((days & 1) == 1) dayCount++;
                days >>= 1;
            }

            // short or long form?
            DateFormatSymbols dfs = new DateFormatSymbols();
            String[] dayList = (dayCount > 1) ?
                    dfs.getShortWeekdays() :
                    dfs.getWeekdays();

            // selected days
            for (int i = 0; i < 7; i++) {
                if ((mDays & (1 << i)) != 0) {
                    ret.append(dayList[DAY_MAP[i]]);
                    dayCount -= 1;
                    if (dayCount > 0) ret.append(
                            context.getText(R.string.day_concat));
                }
            }
            return ret.toString();
        }

        private boolean isSet(int day) {
            return ((mDays & (1 << day)) > 0);
        }

        public void set(int day, boolean set) {
            if (set) {
                mDays |= (1 << day);
            } else {
                mDays &= ~(1 << day);
            }
        }

        public void set(DaysOfWeek dow) {
            mDays = dow.mDays;
        }

        public int getCoded() {
            return mDays;
        }

        // Returns days of week encoded in an array of booleans.
        public boolean[] getBooleanArray() {
            boolean[] ret = new boolean[7];
            for (int i = 0; i < 7; i++) {
                ret[i] = isSet(i);
            }
            return ret;
        }

        public boolean isRepeatSet() {
            return mDays != 0;
        }

        /**
         * returns number of days from today until next alarm
         * @param c must be set to today
         */
        public int getNextAlarm(Calendar c) {
            if (mDays == 0) {
                return -1;
            }

            int today = (c.get(Calendar.DAY_OF_WEEK) + 5) % 7;

            int day = 0;
            int dayCount = 0;
            for (; dayCount < 7; dayCount++) {
                day = (today + dayCount) % 7;
                if (isSet(day)) {
                    break;
                }
            }
            return dayCount;
        }
    }
}
