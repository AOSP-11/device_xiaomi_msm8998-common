/*
 * Copyright (C) 2016 The OmniROM Project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.omnirom.device;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.preference.Preference;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceViewHolder;

import org.omnirom.device.utils.FileUtils;

/**
 * Backlight Preference used to adjust led brightness of buttons.
 *
 * Created by 0ranko0P <ranko0p@outlook.com> on 2019.10.30
 */
public final class BacklightPreference extends Preference implements
        SeekBar.OnSeekBarChangeListener {

    private static final int BACKLIGHT_MIN_BRIGHTNESS = 0;
    private static final int BACKLIGHT_MAX_BRIGHTNESS = 255;
    private static final float PROGRESS_OFFSET = BACKLIGHT_MAX_BRIGHTNESS / 100f;

    private static final String FILE_LED_LEFT  = "/sys/class/leds/button-backlight/brightness";
    private static final String FILE_LED_RIGHT = "/sys/class/leds/button-backlight1/brightness";

    private SeekBar  mSeekBar;
    private TextView mValueText;

    public BacklightPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutResource(R.layout.preference_seek_bar);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
	super.onBindViewHolder(holder);
        mValueText = (TextView) holder.findViewById(R.id.current_value);

	// Todo: W: I/O operation on main thread
        int brValue = Integer.parseInt(currentValue());
        mSeekBar = (SeekBar) holder.findViewById(R.id.seekbar);
        mSeekBar.setMax(BACKLIGHT_MAX_BRIGHTNESS);
        mSeekBar.setProgress(brValue);
        mSeekBar.setOnSeekBarChangeListener(this);
        updateProgress(brValue);
    }

    /**
     * @return	Button brightness value that currently in use
     *
     * */
    public static String currentValue() {
	// Read the value that currently in use, not the one from sp.
	// User might modify this value though some kernel manager.
        return FileUtils.getFileValue(FILE_LED_LEFT, String.valueOf(BACKLIGHT_MAX_BRIGHTNESS));
    }

    private void updateProgress(int progress) {
        mValueText.setText(Integer.toString(Math.round(progress/ PROGRESS_OFFSET)) + "%");
    }

    private static void setValue(int newValue, Context context) {
	String newStrVal = newValue + "";
        FileUtils.writeValue(FILE_LED_LEFT, newStrVal);
        FileUtils.writeValue(FILE_LED_RIGHT, newStrVal);

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putInt(DeviceSettings.KEY_BTN_BRIGHTNESS, newValue);
        editor.apply();
    }

    public static void restore(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        int storedValue = pref.getInt(DeviceSettings.KEY_BTN_BRIGHTNESS, BACKLIGHT_MAX_BRIGHTNESS);
        setValue(storedValue, context);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
        setValue(progress, getContext());
	updateProgress(progress);
    }

    public static boolean isSupported() {
        return FileUtils.isFileWritable(FILE_LED_LEFT) && FileUtils.isFileWritable(FILE_LED_RIGHT);
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}
