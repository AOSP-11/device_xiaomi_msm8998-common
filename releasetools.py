# Copyright (C) 2019 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

import hashlib
import common
import os

TARGET_DIR = os.getenv('OUT')

# Firmware - sagit
def FullOTA_InstallEnd(self):
  self.output_zip.write(os.path.join(TARGET_DIR, "firmware-update/abl.elf"), "firmware-update/abl.elf")
  self.output_zip.write(os.path.join(TARGET_DIR, "firmware-update/BTFM.bin"), "firmware-update/BTFM.bin")
  self.output_zip.write(os.path.join(TARGET_DIR, "firmware-update/cmnlib64.mbn"), "firmware-update/cmnlib64.mbn")
  self.output_zip.write(os.path.join(TARGET_DIR, "firmware-update/cmnlib.mbn"), "firmware-update/cmnlib.mbn")
  self.output_zip.write(os.path.join(TARGET_DIR, "firmware-update/devcfg.mbn"), "firmware-update/devcfg.mbn")
  self.output_zip.write(os.path.join(TARGET_DIR, "firmware-update/hyp.mbn"), "firmware-update/hyp.mbn")
  self.output_zip.write(os.path.join(TARGET_DIR, "firmware-update/keymaster.mbn"), "firmware-update/keymaster.mbn")
  self.output_zip.write(os.path.join(TARGET_DIR, "firmware-update/logfs_ufs_8mb.bin"), "firmware-update/logfs_ufs_8mb.bin")
  self.output_zip.write(os.path.join(TARGET_DIR, "firmware-update/NON-HLOS.bin"), "firmware-update/NON-HLOS.bin")
  self.output_zip.write(os.path.join(TARGET_DIR, "firmware-update/rpm.mbn"), "firmware-update/rpm.mbn")
  self.output_zip.write(os.path.join(TARGET_DIR, "firmware-update/storsec.mbn"), "firmware-update/storsec.mbn")
  self.output_zip.write(os.path.join(TARGET_DIR, "firmware-update/tz.mbn"), "firmware-update/tz.mbn")
  self.output_zip.write(os.path.join(TARGET_DIR, "firmware-update/xbl.elf"), "firmware-update/xbl.elf")

# Write Firmware updater-script
  self.script.AppendExtra('')
  self.script.AppendExtra('# ---- radio update tasks ----')
  self.script.AppendExtra('')
  self.script.AppendExtra('ui_print("Patching firmware images...");')
  self.script.AppendExtra('package_extract_file("firmware-update/cmnlib64.mbn", "/dev/block/bootdevice/by-name/cmnlib64");')
  self.script.AppendExtra('package_extract_file("firmware-update/cmnlib.mbn", "/dev/block/bootdevice/by-name/cmnlib");')
  self.script.AppendExtra('package_extract_file("firmware-update/hyp.mbn", "/dev/block/bootdevice/by-name/hyp");')
  self.script.AppendExtra('package_extract_file("firmware-update/tz.mbn", "/dev/block/bootdevice/by-name/tz");')
  self.script.AppendExtra('package_extract_file("firmware-update/storsec.mbn", "/dev/block/bootdevice/by-name/storsec");')
  self.script.AppendExtra('package_extract_file("firmware-update/abl.elf", "/dev/block/bootdevice/by-name/abl");')
  self.script.AppendExtra('package_extract_file("firmware-update/devcfg.mbn", "/dev/block/bootdevice/by-name/devcfg");')
  self.script.AppendExtra('package_extract_file("firmware-update/keymaster.mbn", "/dev/block/bootdevice/by-name/keymaster");')
  self.script.AppendExtra('package_extract_file("firmware-update/xbl.elf", "/dev/block/bootdevice/by-name/xbl");')
  self.script.AppendExtra('package_extract_file("firmware-update/rpm.mbn", "/dev/block/bootdevice/by-name/rpm");')
  self.script.AppendExtra('package_extract_file("firmware-update/cmnlib64.mbn", "/dev/block/bootdevice/by-name/cmnlib64bak");')
  self.script.AppendExtra('package_extract_file("firmware-update/cmnlib.mbn", "/dev/block/bootdevice/by-name/cmnlibbak");')
  self.script.AppendExtra('package_extract_file("firmware-update/hyp.mbn", "/dev/block/bootdevice/by-name/hypbak");')
  self.script.AppendExtra('package_extract_file("firmware-update/tz.mbn", "/dev/block/bootdevice/by-name/tzbak");')
  self.script.AppendExtra('package_extract_file("firmware-update/abl.elf", "/dev/block/bootdevice/by-name/ablbak");')
  self.script.AppendExtra('package_extract_file("firmware-update/devcfg.mbn", "/dev/block/bootdevice/by-name/devcfgbak");')
  self.script.AppendExtra('package_extract_file("firmware-update/keymaster.mbn", "/dev/block/bootdevice/by-name/keymasterbak");')
  self.script.AppendExtra('package_extract_file("firmware-update/xbl.elf", "/dev/block/bootdevice/by-name/xblbak");')
  self.script.AppendExtra('package_extract_file("firmware-update/rpm.mbn", "/dev/block/bootdevice/by-name/rpmbak");')
  self.script.AppendExtra('package_extract_file("firmware-update/logfs_ufs_8mb.bin", "/dev/block/bootdevice/by-name/logfs");')
  self.script.AppendExtra('package_extract_file("firmware-update/NON-HLOS.bin", "/dev/block/bootdevice/by-name/modem");')
  self.script.AppendExtra('package_extract_file("firmware-update/BTFM.bin", "/dev/block/bootdevice/by-name/bluetooth");')
