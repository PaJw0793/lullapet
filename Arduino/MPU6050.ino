#include <Wire.h>
#include <MPU6050.h>
#include <math.h>

MPU6050 mpu;

float base_ax = 0, base_ay = 0, base_az = 0;
const float alpha = 0.7;

const float softThreshold = 0.1;
const float strongThreshold = 0.6;

const unsigned long softMotionDuration = 700;
const unsigned long cooldown = 1500;

unsigned long lastDetection = 0;
unsigned long softMotionStart = 0;

bool isMoving = false;
bool softTriggered = false;

void setup() {
  Serial.begin(115200);
  Wire.begin();
  mpu.initialize();

  if (!mpu.testConnection()) {
    Serial.println("MPU6050 연결 실패!");
    while (1);
  }

  Serial.println("기준 측정 중...");
  delay(1000);

  long sum_ax = 0, sum_ay = 0, sum_az = 0;
  for (int i = 0; i < 50; i++) {
    int16_t ax, ay, az;
    mpu.getAcceleration(&ax, &ay, &az);
    sum_ax += ax;
    sum_ay += ay;
    sum_az += az;
    delay(20);
  }

  base_ax = sum_ax / 50.0;
  base_ay = sum_ay / 50.0;
  base_az = sum_az / 50.0;

  Serial.println("기준 설정 완료");
}

void loop() {
  int16_t ax_raw, ay_raw, az_raw;
  mpu.getAcceleration(&ax_raw, &ay_raw, &az_raw);

  base_ax = alpha * base_ax + (1 - alpha) * ax_raw;
  base_ay = alpha * base_ay + (1 - alpha) * ay_raw;
  base_az = alpha * base_az + (1 - alpha) * az_raw;

  float delta_ax = (ax_raw - base_ax) / 16384.0;
  float delta_ay = (ay_raw - base_ay) / 16384.0;
  float delta_az = (az_raw - base_az) / 16384.0;

  float magnitude = sqrt(delta_ax * delta_ax + delta_ay * delta_ay + delta_az * delta_az);

  unsigned long now = millis();

  // 디버그 로그 출력
  Serial.print("ΔG: ");
  Serial.print(magnitude, 4);
  Serial.print(" | State: ");
  Serial.println(isMoving ? "Moving" : "Stopped");

  // 부드러운 움직임 감지
  if (magnitude > softThreshold && magnitude < strongThreshold) {
    if (softMotionStart == 0) {
      softMotionStart = now;
    } else if ((now - softMotionStart > softMotionDuration) && !softTriggered) {
      Serial.println("✨ 부드러운 움직임 감지됨");
      isMoving = true;
      softTriggered = true;
      lastDetection = now;
    }
  } else {
    softMotionStart = 0;
    softTriggered = false;
  }

  // 강한 움직임 감지
  if (magnitude >= strongThreshold && now - lastDetection > cooldown && !softTriggered) {
    Serial.println("💥 강한 움직임 감지됨");
    isMoving = true;
    lastDetection = now;
  }

  // 정지 상태 판단
  if (magnitude < softThreshold * 0.5 && isMoving && now - lastDetection > 1000) {
    Serial.println("정지됨");
    isMoving = false;
    softMotionStart = 0;
    softTriggered = false;
  }

  delay(100);
}
