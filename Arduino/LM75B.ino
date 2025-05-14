#include <Wire.h>
#include <ESP8266WiFi.h>
#include <FirebaseESP8266.h>

// Wi-Fi 설정
const char ssid[] = "알고학원";
const char password[] = "0552465556";

// Firebase 설정
#define FIREBASE_HOST "codepare-43e89-default-rtdb.europe-west1.firebasedatabase.app"
#define FIREBASE_AUTH "AIzaSyDl-uISgpsxK4cJIOFc1bYTrdfiLIT6RXE"

FirebaseData firebaseData;
FirebaseAuth auth;
FirebaseConfig config;

// LM75B 주소
#define LM75B_ADDRESS 0x48

void setup() {
  Serial.begin(115200);
  Wire.begin(D2, D1);

  // Wi-Fi 연결
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("\nWi-Fi 연결 완료");

  // Firebase 설정
  config.host = FIREBASE_HOST;
  config.signer.tokens.legacy_token = FIREBASE_AUTH;
  Firebase.begin(&config, &auth);
  Firebase.reconnectWiFi(true);
}

void loop() {
  // 온도 읽기
  float temperature = readLM75BTemp();
  Serial.print("온도: ");
  Serial.println(temperature+4.0);

  // Firebase 업로드
  bool tempOk = Firebase.setFloat(firebaseData, "/sensor/Wearable/device1/sensorData/temperature", temperature);

  if (tempOk) {
    Serial.println("데이터 업로드 성공");
  } else {
    Serial.println("업로드 실패: " + firebaseData.errorReason());
  }

  delay(5000);
}

// LM75B 온도 읽기 함수
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
