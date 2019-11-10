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
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemProperties;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;

import org.omnirom.device.utils.FileUtils;

public class DeviceSettings extends PreferenceFragmentCompat implements
        Preference.OnPreferenceChangeListener {

    private static final String KEY_CATEGORY_DISPLAY = "display";
    private static final String KEY_CATEGORY_KCAL = "kcal";
    private static final String KEY_CATEGORY_HW_BUTTONS = "hw_buttons";
    private static final String KEY_CATEGORY_USB_FASTCHARGE = "usb_fastcharge";

    static final String KEY_BTN_BRIGHTNESS = "btn_brightness";

    private static final String SPECTRUM_KEY = "spectrum";
    private static final String SPECTRUM_DEFAULT_PROFILE = "0";
    private static final String SPECTRUM_SYSTEM_PROPERTY = "persist.spectrum.profile";

    static final String S2S_KEY = "sweep2sleep";
    static final String KEY_S2S_VIBSTRENGTH = "s2s_vib_strength";
    static final String FILE_S2S_TYPE = "/sys/sweep2sleep/sweep2sleep";

    static final String BUTTONS_SWAP_KEY = "buttons_swap";
    static final String BUTTONS_SWAP_PATH = "/proc/touchpanel/reversed_keys_enable";

    static final String USB_FASTCHARGE_KEY = "fastcharge";
    static final String USB_FASTCHARGE_PATH = "/sys/kernel/fast_charge/force_fast_charge";

    private final String KEY_DEVICE_DOZE = "device_doze";
    private final String KEY_DEVICE_DOZE_PACKAGE_NAME = "org.lineageos.settings.doze";

    private SwitchPreference mButtonSwap;
    private SwitchPreference mFastCharge;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.main, rootKey);

        PreferenceScreen prefSet = getPreferenceScreen();

        ListPreference mS2S = findPreference(S2S_KEY);
        mS2S.setValue(FileUtils.getFileValue(FILE_S2S_TYPE, "0"));
        mS2S.setOnPreferenceChangeListener(this);

        if (FileUtils.isFileWritable(BUTTONS_SWAP_PATH)) {
            mButtonSwap = findPreference(BUTTONS_SWAP_KEY);
            mButtonSwap.setChecked(FileUtils.getFileValueAsBoolean(BUTTONS_SWAP_PATH, false));
            mButtonSwap.setOnPreferenceChangeListener(this);
        } else {
            PreferenceCategory mHWButtons = prefSet.findPreference(KEY_CATEGORY_HW_BUTTONS);
            prefSet.removePreference(mHWButtons);
        }

        if (FileUtils.isFileWritable(USB_FASTCHARGE_PATH)) {
            mFastCharge = findPreference(USB_FASTCHARGE_KEY);
            mFastCharge.setChecked(FileUtils.getFileValueAsBoolean(USB_FASTCHARGE_PATH, false));
            mFastCharge.setOnPreferenceChangeListener(this);
        } else {
            PreferenceCategory mUsbFastcharge = prefSet.findPreference(KEY_CATEGORY_USB_FASTCHARGE);
            prefSet.removePreference(mUsbFastcharge);
        }

        ListPreference mSPECTRUM = findPreference(SPECTRUM_KEY);
        if (mSPECTRUM != null) {
            mSPECTRUM.setValue(SystemProperties.get(SPECTRUM_SYSTEM_PROPERTY, SPECTRUM_DEFAULT_PROFILE));
            mSPECTRUM.setOnPreferenceChangeListener(this);
        }

        S2SVibratorStrengthPreference mVibratorStrengthS2S = findPreference(KEY_S2S_VIBSTRENGTH);
        if (mVibratorStrengthS2S != null) {
            mVibratorStrengthS2S.setEnabled(S2SVibratorStrengthPreference.isSupported());
        }

        if (!isAppInstalled(KEY_DEVICE_DOZE_PACKAGE_NAME)) {
            PreferenceCategory displayCategory = findPreference(KEY_CATEGORY_DISPLAY);
            displayCategory.removePreference(findPreference(KEY_DEVICE_DOZE));
        }
    }

    static void restoreSpectrumProp(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String spectrumStoredValue = sp.getString(SPECTRUM_KEY, SPECTRUM_DEFAULT_PROFILE);
        SystemProperties.set(SPECTRUM_SYSTEM_PROPERTY, spectrumStoredValue);
    }

    private void setButtonSwap(boolean value) {
        FileUtils.writeValue(BUTTONS_SWAP_PATH, value ? "1" : "0");
    }

    private void setFastCharge(boolean value) {
        FileUtils.writeValue(USB_FASTCHARGE_PATH, value ? "1" : "0");
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        if (KEY_CATEGORY_KCAL.equals(preference.getKey())) {
            DisplayCalibrationActivity.startActivity(getContext());
            return true;
        }
        return super.onPreferenceTreeClick(preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String newStrVal = newValue.toString();
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();

        switch (preference.getKey()) {
            case S2S_KEY: {
                FileUtils.writeValue(FILE_S2S_TYPE, newStrVal);
                editor.putString(S2S_KEY, newStrVal).apply();
                return true;
            }
            case SPECTRUM_KEY: {
                SystemProperties.set(SPECTRUM_SYSTEM_PROPERTY, newStrVal);
                editor.putString(SPECTRUM_KEY, newStrVal).apply();
                return true;
            }
            case BUTTONS_SWAP_KEY: {
                boolean value = (Boolean) newValue;
                mButtonSwap.setChecked(value);
                setButtonSwap(value);
                editor.putBoolean(BUTTONS_SWAP_KEY, value).apply();
                return true;
            }
            case USB_FASTCHARGE_KEY: {
                boolean value = (Boolean) newValue;
                mFastCharge.setChecked(value);
                setFastCharge(value);
                editor.putBoolean(USB_FASTCHARGE_KEY, value).apply();
                return true;
            }
            default:
                return false;
        }
    }

    private boolean isAppInstalled(String uri) {
        PackageManager pm = getContext().getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return false;
    }
}
