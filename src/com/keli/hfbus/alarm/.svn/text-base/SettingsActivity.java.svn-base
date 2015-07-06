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

import com.keli.hfbus.HFBusApp;
import com.keli.hfbus.R;

import android.media.AudioManager;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;

/**
 * Settings for the Alarm Clock.
 */
public class SettingsActivity extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener,OnClickListener{

    private static final int ALARM_STREAM_TYPE_BIT =
            1 << AudioManager.STREAM_ALARM;

    private static final String KEY_ALARM_IN_SILENT_MODE =
            "alarm_in_silent_mode";
    static final String KEY_ALARM_SNOOZE =
            "snooze_duration";
    //到站误差时间
    static final String KEY_ALARM_TIME =
            "time_duration";
    static final String KEY_VOLUME_BEHAVIOR =
            "volume_button_setting";
    public static String DEFAULT_TIME = "5";//默认误差时间
    private ImageButton ibtnTitleReturn;
    HFBusApp mApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    	getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.title_bar);
    	 mApp = (HFBusApp) getApplication();
         mApp.getmActivityManager().add(this);
    	 ibtnTitleReturn=(ImageButton)findViewById(R.id.ibtnTitleReturn);
         ibtnTitleReturn.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
            Preference preference) {
        if (KEY_ALARM_IN_SILENT_MODE.equals(preference.getKey())) {
            CheckBoxPreference pref = (CheckBoxPreference) preference;
            int ringerModeStreamTypes = Settings.System.getInt(
                    getContentResolver(),
                    Settings.System.MODE_RINGER_STREAMS_AFFECTED, 0);

            if (pref.isChecked()) {
                ringerModeStreamTypes &= ~ALARM_STREAM_TYPE_BIT;
            } else {
                ringerModeStreamTypes |= ALARM_STREAM_TYPE_BIT;
            }

            Settings.System.putInt(getContentResolver(),
                    Settings.System.MODE_RINGER_STREAMS_AFFECTED,
                    ringerModeStreamTypes);

            return true;
        }

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    public boolean onPreferenceChange(Preference pref, Object newValue) {
        final ListPreference listPref = (ListPreference) pref;
        final int idx = listPref.findIndexOfValue((String) newValue);
        listPref.setSummary(listPref.getEntries()[idx]);
        return true;
    }

    private void refresh() {
        final CheckBoxPreference alarmInSilentModePref =
                (CheckBoxPreference) findPreference(KEY_ALARM_IN_SILENT_MODE);
        final int silentModeStreams =
                Settings.System.getInt(getContentResolver(),
                        Settings.System.MODE_RINGER_STREAMS_AFFECTED, 0);
        alarmInSilentModePref.setChecked(
                (silentModeStreams & ALARM_STREAM_TYPE_BIT) == 0);

//        final ListPreference snooze =
//                (ListPreference) findPreference(KEY_ALARM_SNOOZE);
//        snooze.setSummary(snooze.getEntry());
//        snooze.setOnPreferenceChangeListener(this);
        
        final ListPreference time =
                (ListPreference) findPreference(KEY_ALARM_TIME);
        time.setSummary(time.getEntry());
        time.setOnPreferenceChangeListener(this);
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.ibtnTitleReturn:
			super.onBackPressed();
			break;
		}
	}

}
