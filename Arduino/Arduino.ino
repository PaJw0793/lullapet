#include <ESP8266WiFi.h>
#include <FirebaseESP8266.h>

// Wi-Fi 설정
const char* ssid = "알고학원";
const char* password = "0552465556";

// Firebase 설정
#define FIREBASE_HOST "codepare-43e89-default-rtdb.europe-west1.firebasedatabase.app"
#define FIREBASE_AUTH "AIzaSyDl-uISgpsxK4cJIOFc1bYTrdfiLIT6RXE"

FirebaseData firebaseData;
FirebaseAuth auth;
FirebaseConfig config;

void setup() {
    Serial.begin(115200);

    // Wi-Fi 연결
    WiFi.begin(ssid, password);
    Serial.print("Wi-Fi 연결 중...");
    while (WiFi.status() != WL_CONNECTED) {
        delay(500);
        Serial.print(".");
    }
    Serial.println("\nWi-Fi 연결 완료!");
    Serial.print("IP 주소: ");
    Serial.println(WiFi.localIP());

    // Firebase 설정
    config.host = FIREBASE_HOST;
    config.signer.tokens.legacy_token = FIREBASE_AUTH;

    Firebase.begin(&config, &auth);
    Firebase.reconnectWiFi(true);

    Serial.println("Firebase 연결 완료!");
}

void loop() {
    // 1. Firebase에 온도 데이터 업로드
    float temperature = random(20, 30); // 가상의 온도 데이터 생성
    if (Firebase.setFloat(firebaseData, "/sensor/Wearable/device2/sensorData/heartRate", temperature)) {
        Serial.println("온도 데이터 업로드 성공: " + String(temperature));
    } else {
        Serial.println("온도 데이터 업로드 실패: " + firebaseData.errorReason());
    }

    // 2. Firebase에서 값 읽기 테스트
    if (Firebase.getInt(firebaseData, "/sensor/Wearable/device2/sensorData/heartRate")) {
        int value = firebaseData.intData();
        Serial.println("Firebase 값 확인됨: " + String(value));
    } else {
        Serial.println("값 읽기 실패: " + firebaseData.errorReason());
    }

    delay(5000); // 5초 대기 후 반복
}
