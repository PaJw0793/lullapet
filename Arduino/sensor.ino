#include <Wire.h>
#include <ESP8266WiFi.h>
#include <FirebaseESP8266.h>
#include "MAX30105.h"
#include <MPU6050.h>

// ========== Wi-Fi 설정 ==========
const char ssid[] = "알고학원";
const char password[] = "0552465556";

// ========== Firebase 설정 ==========
#define FIREBASE_HOST "codepare-43e89-default-rtdb.europe-west1.firebasedatabase.app"
#define FIREBASE_AUTH "AIzaSyDl-uISgpsxK4cJIOFc1bYTrdfiLIT6RXE"
FirebaseData firebaseData;
FirebaseAuth auth;
FirebaseConfig config;

// ========== 센서 객체 ==========
MAX30105 maxSensor;
MPU6050 mpu;
#define LM75B_ADDRESS 0x48

// ========== 움직임 감지용 변수 ==========
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
  Wire.begin(D2, D1); // I2C 설정

  // Wi-Fi 연결
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500); Serial.print(".");
  }
  Serial.println("\nWi-Fi 연결 완료");

  // Firebase 설정
  config.host = FIREBASE_HOST;
  config.signer.tokens.legacy_token = FIREBASE_AUTH;
  Firebase.begin(&config, &auth);
  Firebase.reconnectWiFi(true);

  // MAX30102 초기화
  if (!maxSensor.begin(Wire, I2C_SPEED_STANDARD)) {
    Serial.println("MAX30102 연결 실패"); while (1);
  }
  maxSensor.setup();
  maxSensor.setPulseAmplitudeRed(0x0A);
  maxSensor.setPulseAmplitudeIR(0x00);

  // MPU6050 초기화
  mpu.initialize();
  if (!mpu.testConnection()) {
    Serial.println("MPU6050 연결 실패!"); while (1);
  }

  // 기준값 측정
  long sum_ax = 0, sum_ay = 0, sum_az = 0;
  for (int i = 0; i < 50; i++) {
    int16_t ax, ay, az;
    mpu.getAcceleration(&ax, &ay, &az);
    sum_ax += ax; sum_ay += ay; sum_az += az;
    delay(20);
  }
  base_ax = sum_ax / 50.0;
  base_ay = sum_ay / 50.0;
  base_az = sum_az / 50.0;

  Serial.println("초기화 완료");
}

void loop() {
  // ===== 센서 데이터 수집 =====
  long redValue = maxSensor.getRed();
  float temperature = readLM75BTemp();

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

  // ===== 움직임 감지 로직 =====
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

  if (magnitude >= strongThreshold && now - lastDetection > cooldown && !softTriggered) {
    Serial.println("💥 강한 움직임 감지됨");
    isMoving = true;
    lastDetection = now;
  }

  if (magnitude < softThreshold * 0.5 && isMoving && now - lastDetection > 1000) {
    Serial.println("정지됨");
    isMoving = false;
    softMotionStart = 0;
    softTriggered = false;
  }

  // ===== Serial 출력 =====
  Serial.print("온도: ");
  Serial.print(temperature + 4.0); // 보정치 적용
  Serial.print(" °C | Red: ");
  Serial.print(redValue);
  Serial.print(" | ΔG: ");
  Serial.println(magnitude, 4);

  // ===== Firebase 업로드 =====
  Firebase.setFloat(firebaseData, "/sensor/Wearable/device1/sensorData/temperature", temperature);
  Firebase.setInt(firebaseData, "/sensor/Wearable/device1/sensorData/red", redValue);
  Firebase.setFloat(firebaseData, "/sensor/Wearable/device1/sensorData/motion", magnitude);

  delay(2000);
}

// ========== LM75B 온도 읽기 ==========
float readLM75BTemp() {
  Wire.beginTransmission(LM75B_ADDRESS);
  Wire.write(0x00);
  Wire.endTransmission(false);
  Wire.requestFrom(LM75B_ADDRESS, 2);

  if (Wire.available() == 2) {
    byte msb = Wire.read();
    byte lsb = Wire.read();
    int16_t rawTemp = ((msb << 8) | lsb) >> 5;
    if (rawTemp > 1023) rawTemp -= 2048;
    return rawTemp * 0.125;
  }
  return -1000.0;
}
