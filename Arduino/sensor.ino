#include <Wire.h>
#include <ESP8266WiFi.h>
#include <FirebaseESP8266.h>
#include "MAX30105.h" // SparkFun MAX3010x Pulse and Proximity Sensor Library

// ========== Wi-Fi 설정 ==========
const char ssid[] = "알고학원";
const char password[] = "0552465556";

// ========== Firebase 설정 ==========
#define FIREBASE_HOST "codepare-43e89-default-rtdb.europe-west1.firebasedatabase.app"
#define FIREBASE_AUTH "AIzaSyDl-uISgpsxK4cJIOFc1bYTrdfiLIT6RXE"
FirebaseData firebaseData; // Firebase 데이터 객체 선언
FirebaseAuth auth;         // Firebase 인증 객체 선언
FirebaseConfig config;     // Firebase 설정 객체 선언

// ========== 센서 객체 ==========
MAX30105 particleSensor; // MAX30105 센서 객체
#define LM75B_ADDRESS 0x48 // LM75B 온도 센서 I2C 주소

// ========== 심박수 측정 관련 변수 (로컬 최대값 감지) ==========
const int IR_BUFFER_SIZE = 20;
long irBuffer[IR_BUFFER_SIZE];
int irBufferIndex = 0;
unsigned long lastPeakTime = 0;
int heartRate = 0;
const int PEAK_THRESHOLD = 5000; // 피크로 판단하기 위한 IR 값 임계값 (조절 필요)

void setup() {
  Serial.begin(115200);
  Wire.begin(D2, D1); // SDA, SCL 순서

  Serial.println("Wi-Fi 연결 중...");
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500); Serial.print(".");
  }
  Serial.println("\nWi-Fi 연결 완료");
  Serial.print("IP 주소: ");
  Serial.println(WiFi.localIP());

  // Firebase 설정 초기화
  config.host = FIREBASE_HOST;
  config.signer.tokens.legacy_token = FIREBASE_AUTH;
  Firebase.begin(&config, &auth);
  Firebase.reconnectWiFi(true);

  // MAX30105 초기화
  if (!particleSensor.begin(Wire, I2C_SPEED_STANDARD)) {
    Serial.println("MAX30105 연결 실패! 배선 또는 전원을 확인하세요.");
    while (1); // 무한 루프
  }
  Serial.println("MAX30105 초기화 완료");
  particleSensor.setup(); // 기본 설정 적용
  particleSensor.setPulseAmplitudeRed(0x0A); // Red LED 밝기 설정
  particleSensor.setPulseAmplitudeIR(0x0F);  // IR LED 밝기 설정 (심박수 측정에 중요)

  Serial.println("모든 센서 및 Wi-Fi 초기화 완료");
}

void loop() {
  long currentIR = particleSensor.getIR();
  irBuffer[irBufferIndex] = currentIR;
  irBufferIndex = (irBufferIndex + 1) % IR_BUFFER_SIZE;

  unsigned long now = millis();
  bool isLocalMax = true;
  int currentIndex = irBufferIndex == 0 ? IR_BUFFER_SIZE - 1 : irBufferIndex - 1;

  // 최근 5개 값 비교 (로컬 최대값 감지)
  for (int i = 1; i < 5; i++) {
    int compareIndex = (currentIndex - i + IR_BUFFER_SIZE) % IR_BUFFER_SIZE;
    if (irBuffer[currentIndex] <= irBuffer[compareIndex]) {
      isLocalMax = false;
      break;
    }
  }

  if (isLocalMax && irBuffer[currentIndex] > PEAK_THRESHOLD) {
    unsigned long timeDiff = now - lastPeakTime;
    if (timeDiff > 200 && timeDiff < 2000) { // 200ms = 300bpm, 2000ms = 30bpm
      int calculatedBPM = 60000 / timeDiff;
      if (calculatedBPM > 20 && calculatedBPM < 200) {
        heartRate = calculatedBPM;
        lastPeakTime = now;
      }
    }
  }

  float temperature = readLM75BTemp();

  // ===== Serial 출력 =====
  Serial.print("IR: ");
  Serial.print(currentIR);
  Serial.print(" | 심박수: ");
  Serial.print(heartRate);
  Serial.print(" bpm | 온도: ");
  Serial.println(temperature + 4.0);

  // ===== Firebase 업로드 =====
  if (Firebase.ready() && WiFi.status() == WL_CONNECTED) {
    Firebase.setFloat(firebaseData, "/sensor/Wearable/device1/sensorData/temperature", temperature + 4.0);
    Firebase.setInt(firebaseData, "/sensor/Wearable/device1/sensorData/heartRate", heartRate);
  } else {
    Serial.println("Firebase 연결 또는 Wi-Fi 상태 불안정");
  }

  delay(50); // 데이터 수집 빈도 증가
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