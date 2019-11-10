/*
* Copyright (C) 2013 The OmniROM Project
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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import org.omnirom.device.utils.FileUtils;

public class Startup extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent bootintent) {
        S2SVibratorStrengthPreference.restore(context);
        String storedValue = PreferenceManager.getDefaultSharedPreferences(context).getString(DeviceSettings.S2S_KEY, "0");
        FileUtils.writeValue(DeviceSettings.FILE_S2S_TYPE, storedValue);
        DeviceSettings.restoreSpectrumProp(context);
        boolean btnSwapStoredValue = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(DeviceSettings.BUTTONS_SWAP_KEY, false);
        FileUtils.writeValue(DeviceSettings.BUTTONS_SWAP_PATH, btnSwapStoredValue ? "1" : "0");
        boolean usbFastchargeStoredValue = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(DeviceSettings.USB_FASTCHARGE_KEY, false);
        FileUtils.writeValue(DeviceSettings.USB_FASTCHARGE_PATH, usbFastchargeStoredValue ? "1" : "0" );
        DisplayCalibration.restore(context);

        BacklightPreference.restore(context);
    }
}
