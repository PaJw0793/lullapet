#include <ESP8266WiFi.h>
#include <FirebaseESP8266.h>

// Wi-Fi 설정
const char* ssid = "알고학원";        // Wi-Fi 이름
const char* password = "0552465556"; // Wi-Fi 비밀번호

// Firebase 설정
#define FIREBASE_HOST "codepare-43e89-default-rtdb.europe-west1.firebasedatabase.app"  // Firebase 실시간 데이터베이스 URL
#define FIREBASE_AUTH "AIzaSyDl-uISgpsxK4cJIOFc1bYTrdfiLIT6RXE"  // Firebase 인증 토큰

FirebaseData firebaseData;  // Firebase 데이터 객체
FirebaseAuth auth;          // Firebase 인증 객체
FirebaseConfig config;      // Firebase 설정 객체

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

    Firebase.begin(&config, &auth);  // Firebase 시작
    Firebase.reconnectWiFi(true);

    Serial.println("Firebase 연결 완료!");

    // Firebase에 데이터 전송 테스트
    if (Firebase.setString(firebaseData, "/sensor/temp", "25.5")) {
        Serial.println("데이터 업로드 성공!");
    } else {
        Serial.println("데이터 업로드 실패: " + firebaseData.errorReason());
    }
}

void loop() {
    // 10초마다 Firebase 업데이트
    float temperature = random(20, 30); // 임의 온도값 생성
    if (Firebase.setFloat(firebaseData, "/sensor/temp", temperature)) {
        Serial.println("업데이트 완료: " + String(temperature));
    } else {
        Serial.println("업데이트 실패: " + firebaseData.errorReason());
    }
    delay(10000); // 10초 대기
}
