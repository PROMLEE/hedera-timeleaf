# Hedera + Thymeleaf 데모

간단한 Spring Boot 애플리케이션으로 Hedera Testnet에 연결하여 계정 잔액을 조회합니다.

## 요구 사항

- Java 17+
- Maven 3.9+
- (선택) Hedera 테스트넷 운영자 자격 증명 환경변수
  - `HEDERA_ACCOUNT_ID` (예: 0.0.1234)
  - `HEDERA_PRIVATE_KEY` (예: 302e020100300506032b657004220420...)

## 실행 방법 (Windows cmd)

1. 의존성 설치 및 실행

```
mvn -q -e -DskipTests clean package
mvn spring-boot:run
```

2. 브라우저에서 열기

- http://localhost:8080

3. 폼에 Hedera 계정 ID를 입력하면 잔액을 조회합니다. 운영자 환경변수는 잔액 조회에 필요하지 않습니다.

## 구성

- `pom.xml`: Spring Boot, Thymeleaf, Hedera Java SDK 의존성
- `src/main/resources/application.yaml`: 기본 설정 (testnet)
- `src/main/java/com/example/hedera/service/HederaService.java`: Hedera 연결 및 잔액 조회
- `src/main/java/com/example/hedera/web/HomeController.java`: 라우팅 및 뷰 모델
- `src/main/resources/templates/index.html`: Thymeleaf 템플릿

## 참고

- 네트워크는 기본 `testnet`이며 `application.yaml`에서 변경 가능
- 환경변수 설정 예 (cmd):

```
set HEDERA_ACCOUNT_ID=0.0.1234
set HEDERA_PRIVATE_KEY=302e0201...
```
