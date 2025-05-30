#include <Wire.h>
#include <ESP8266WiFi.h>
#include <FirebaseESP8266.h>
#include "MAX30105.h"
#include "heartRate.h"

// Wi-Fi 설정
const char ssid[] = "jiwon";
const char password[] = "jiwon0930!";

// Firebase 설정
#define FIREBASE_HOST "codepare-43e89-default-rtdb.europe-west1.firebasedatabase.app"
#define FIREBASE_AUTH "AIzaSyDl-uISgpsxK4cJIOFc1bYTrdfiLIT6RXE"
FirebaseData firebaseData;
FirebaseAuth auth;
FirebaseConfig config;

// 센서 객체
MAX30105 particleSensor;
#define LM75B_ADDRESS 0x48

// 심박수 변수
const int IR_BUFFER_SIZE = 20;
long irBuffer[IR_BUFFER_SIZE];
int irBufferIndex = 0;
unsigned long lastPeakTime = 0;

const long PEAK_THRESHOLD = 15000;  // IR 임계값 (환경에 따라 조절)

// 로컬 최대값 판단에 쓸 비교 윈도우 크기
const int LOCAL_MAX_WINDOW = 5;

const byte RATE_SIZE = 4; //Increase this for more averaging. 4 is good.
byte rates[RATE_SIZE]; //Array of heart rates
byte rateSpot = 0;
long lastBeat = 0; //Time at which the last beat occurred

float beatsPerMinute;
int beatAvg;

void setup() {
  Serial.begin(115200);
  Wire.begin(D2, D1);

  Serial.println("Wi-Fi 연결 중...");
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("\nWi-Fi 연결 완료");
  Serial.print("IP 주소: ");
  Serial.println(WiFi.localIP());

  config.host = FIREBASE_HOST;
  config.signer.tokens.legacy_token = FIREBASE_AUTH;
  Firebase.begin(&config, &auth);
  Firebase.reconnectWiFi(true);

  if (!particleSensor.begin(Wire, I2C_SPEED_STANDARD)) {
    Serial.println("MAX30105 연결 실패! 배선 또는 전원 확인");
    while (1);
  }
  Serial.println("MAX30105 초기화 완료");

  // 센서 설정: 샘플레이트 60, LED 밝기 최고 (0x3F)
  particleSensor.setup(60, 4, 2, 100, 411, 4096);
  particleSensor.setPulseAmplitudeRed(0x3F);
  particleSensor.setPulseAmplitudeIR(0x3F);

  Serial.println("센서 및 Wi-Fi 초기화 완료");

  lastPeakTime = 0;  // 초기화
}

void loop() {
  float temperature = readLM75BTemp();
  float heartRate = getHeartrate();


  Serial.print(" | 심박수: ");
  Serial.print(heartRate);
  Serial.print(" bpm | 온도: ");
  Serial.println(temperature + 4.0);

  if (Firebase.ready() && WiFi.status() == WL_CONNECTED) {
    Firebase.setFloat(firebaseData, "/sensor/Wearable/device1/sensorData/temperature", temperature + 4.0);
    Firebase.setInt(firebaseData, "/sensor/Wearable/device1/sensorData/heartRate", heartRate);
  } else {
    Serial.println("Firebase 연결 또는 Wi-Fi 상태 불안정");
  }

  delay(50);
}

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

float getHeartrate() {
  long irValue = particleSensor.getIR();

  if (checkForBeat(irValue) == true)
  {
    //We sensed a beat!
    long delta = millis() - lastBeat;
    lastBeat = millis();

    beatsPerMinute = 60 / (delta / 1000.0);

    if (beatsPerMinute < 255 && beatsPerMinute > 20)
    {
      rates[rateSpot++] = (byte)beatsPerMinute; 
      rateSpot %= RATE_SIZE; 

      
      beatAvg = 0;
      for (byte x = 0 ; x < RATE_SIZE ; x++)
        beatAvg += rates[x];
      beatAvg /= RATE_SIZE;
    }
  }
  return beatsPerMinute;
}