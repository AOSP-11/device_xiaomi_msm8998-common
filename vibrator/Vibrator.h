#ifndef ANDROID_HARDWARE_VIBRATOR_V1_1_VIBRATOR_H
#define ANDROID_HARDWARE_VIBRATOR_V1_1_VIBRATOR_H

#include <android/hardware/vibrator/1.1/IVibrator.h>
#include <hidl/Status.h>

namespace android {
namespace hardware {
namespace vibrator {
namespace V1_1 {
namespace implementation {

class Vibrator : public IVibrator {
public:
  Vibrator();

  // Methods from ::android::hardware::vibrator::V1_0::IVibrator follow.
  using Status = ::android::hardware::vibrator::V1_0::Status;
  Return<Status> on(uint32_t timeoutMs) override;
  Return<Status> off() override;
  Return<bool> supportsAmplitudeControl() override;
  Return<Status> setAmplitude(uint8_t amplitude) override;

  using EffectStrength = ::android::hardware::vibrator::V1_0::EffectStrength;
  using Effect = ::android::hardware::vibrator::V1_0::Effect;
  Return<void> perform(Effect effect, EffectStrength strength, perform_cb _hidl_cb) override;

  // Methods from ::android::hardware::vibrator::V1_0::IVibrator follow.
  Return<void> perform_1_1(Effect_1_1 effect, EffectStrength strength, perform_cb _hidl_cb) override;

private:
  uint32_t minVoltage;
  uint32_t maxVoltage;
};

}  // namespace implementation
}  // namespace V1_1
}  // namespace vibrator
}  // namespace hardware
}  // namespace android

#endif  // ANDROID_HARDWARE_VIBRATOR_V1_1_VIBRATOR_H
