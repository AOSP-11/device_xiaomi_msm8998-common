/*
 * Copyright (C) 2019 The OmniROM Project
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

package org.omnirom.device.Preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.widget.SeekBar;

import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import org.omnirom.device.R;
import org.omnirom.device.utils.FileUtils;

public final class S2SVibratorStrengthPreference extends Preference implements
        SeekBar.OnSeekBarChangeListener {

    public static final String KEY_S2S_VIBSTRENGTH = "s2s_vib_strength";
    private static final String VIB_STRENGTH_FILE = "/sys/sweep2sleep/vib_strength";

    private static final int VIB_STRENGTH_MIN = 0;
    private static final int VIB_STRENGTH_MAX = 90;
    private static final int VIB_STRENGTH_DEFAULT = 20;

    private SeekBar mSeekBar;
    private Vibrator mVibrator;

    public static final class KernelFeatureImp implements KernelFeature<Integer> {

        private KernelFeatureImp(){}

        @Override
        public boolean isSupported() {
            return FileUtils.isFileWritable(VIB_STRENGTH_FILE);
        }

        @Override
        public Integer getCurrentValue() {
            String currentVal = FileUtils.getFileValue(VIB_STRENGTH_FILE, null);
            return currentVal == null? VIB_STRENGTH_DEFAULT : Integer.valueOf(currentVal);
        }

        @Override
        public boolean applyValue(Integer newValue) {
            return FileUtils.writeValue(VIB_STRENGTH_FILE, newValue.toString());
        }

        @Override
        public void applySharedPreferences(Integer newValue, SharedPreferences sp) {
            sp.edit().putInt(KEY_S2S_VIBSTRENGTH, newValue).apply();
        }

        @Override
        public boolean restore(SharedPreferences sp) {
            if(!isSupported()) return false;

            int storedValue = sp.getInt(KEY_S2S_VIBSTRENGTH, VIB_STRENGTH_DEFAULT);
            return applyValue(storedValue);
        }
    }

    public static KernelFeatureImp FEATURE = new KernelFeatureImp();

    public S2SVibratorStrengthPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutResource(R.layout.preference_seek_bar);
        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }


    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);

        mSeekBar = (SeekBar) holder.findViewById(R.id.seekbar);
        mSeekBar.setMax(VIB_STRENGTH_MAX - VIB_STRENGTH_MIN);
        mSeekBar.setProgress(FEATURE.getCurrentValue() - VIB_STRENGTH_MIN);
        mSeekBar.setOnSeekBarChangeListener(this);
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
        FEATURE.applyValue(progress + VIB_STRENGTH_MIN);
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        FEATURE.applySharedPreferences(mSeekBar.getProgress(), getSharedPreferences());
        mVibrator.vibrate(mSeekBar.getProgress());
    }
}
