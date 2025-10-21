# 🔧 문제 해결 가이드

Hedera Timeleaf 애플리케이션 사용 중 발생할 수 있는 문제와 해결 방법입니다.

## 📑 목차

1. [애플리케이션 시작 문제](#1-애플리케이션-시작-문제)
2. [Operator 설정 문제](#2-operator-설정-문제)
3. [트랜잭션 실행 문제](#3-트랜잭션-실행-문제)
4. [네트워크 연결 문제](#4-네트워크-연결-문제)
5. [Java 및 Maven 문제](#5-java-및-maven-문제)
6. [Thymeleaf 템플릿 문제](#6-thymeleaf-템플릿-문제)

---

## 1. 애플리케이션 시작 문제

### 문제 1.1: "Port 8080 was already in use"

**증상:**

```
Web server failed to start. Port 8080 was already in use.
Action: Identify and stop the process that's listening on port 8080 or configure this application to listen on another port.
```

**원인:**

- 다른 프로세스가 이미 포트 8080을 사용 중
- 이전에 실행한 애플리케이션이 종료되지 않음

**해결 방법:**

#### 방법 1: 프로세스 종료

**Windows Command Prompt:**

```cmd
# 1. 포트 8080을 사용하는 프로세스 찾기
netstat -ano | findstr :8080

# 출력 예시:
#   TCP    0.0.0.0:8080           0.0.0.0:0              LISTENING       12345
#                                                                          ^^^^^ PID

# 2. 해당 프로세스 종료 (PID를 실제 번호로 변경)
taskkill /F /PID 12345

# 3. 애플리케이션 재시작
.\mvnw.cmd spring-boot:run
```

**Windows PowerShell:**

```powershell
# 1. 포트 8080을 사용하는 프로세스 찾기
Get-NetTCPConnection -LocalPort 8080 | Select-Object -Property OwningProcess

# 2. 프로세스 종료
Stop-Process -Id 12345 -Force

# 3. 애플리케이션 재시작
.\mvnw.cmd spring-boot:run
```

#### 방법 2: 다른 포트 사용

**application.yaml 수정:**

```yaml
server:
  port: 8081 # 8080 → 8081로 변경
```

그런 다음:

```
http://localhost:8081
```

---

### 문제 1.2: "java: invalid source release: 20"

**증상:**

```
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-compiler-plugin:3.11.0:compile
[ERROR] Fatal error compiling: error: invalid source release: 20
```

**원인:**

- 설치된 Java 버전이 20 미만
- JAVA_HOME이 올바르게 설정되지 않음

**해결 방법:**

#### 1. Java 버전 확인

```cmd
java -version
```

예상 출력:

```
java version "20.0.2" 2023-07-18
```

#### 2. Java 20 설치

Java 20 이상이 없으면 설치:

```
https://jdk.java.net/20/
```

#### 3. JAVA_HOME 설정

**Windows 시스템 환경 변수:**

1. `Win + R` → `sysdm.cpl`
2. "고급" 탭 → "환경 변수"
3. 시스템 변수에서 `JAVA_HOME` 추가:
   - 변수 이름: `JAVA_HOME`
   - 변수 값: `C:\Program Files\Java\jdk-20`
4. `Path`에 `%JAVA_HOME%\bin` 추가
5. 모든 터미널 창 재시작

#### 4. 확인

```cmd
echo %JAVA_HOME%
java -version
javac -version
```

---

### 문제 1.3: "mvnw.cmd is not recognized"

**증상:**

```
'mvnw.cmd' is not recognized as an internal or external command
```

**원인:**

- 현재 디렉토리에 `mvnw.cmd` 파일이 없음
- 잘못된 디렉토리에서 명령 실행

**해결 방법:**

#### 1. 올바른 디렉토리로 이동

```cmd
cd d:\local_project\hedera-timeleaf
dir mvnw.cmd
```

`mvnw.cmd` 파일이 보여야 합니다.

#### 2. Maven Wrapper 파일이 없는 경우

프로젝트를 다시 클론하거나 다운로드하세요.

#### 3. 시스템 Maven 사용 (대안)

Maven이 설치되어 있다면:

```cmd
mvn spring-boot:run
```

---

## 2. Operator 설정 문제

### 문제 2.1: "Operator: Not Configured" 표시

**증상:**

- 웹 페이지에서 "🔴 Operator: Not Configured" 배지 표시
- 트랜잭션 기능이 비활성화됨

**원인:**

- 환경 변수 `HEDERA_ACCOUNT_ID` 또는 `HEDERA_PRIVATE_KEY`가 설정되지 않음

**해결 방법:**

#### 1. 환경 변수 확인

```cmd
echo %HEDERA_ACCOUNT_ID%
echo %HEDERA_PRIVATE_KEY%
```

출력이 비어있거나 변수 이름 그대로 표시되면 설정되지 않은 것입니다.

#### 2. 환경 변수 설정

```cmd
set HEDERA_ACCOUNT_ID=0.0.1234567
set HEDERA_PRIVATE_KEY=302e020100300506032b657004220420abcd1234...
```

#### 3. 애플리케이션 재시작

```cmd
.\mvnw.cmd spring-boot:run
```

#### 4. 브라우저 새로고침

```
http://localhost:8080
```

"🟢 Operator: Configured"가 표시되어야 합니다.

---

### 문제 2.2: 환경 변수가 애플리케이션에 전달되지 않음

**증상:**

- 환경 변수를 설정했지만 여전히 "Not Configured" 표시
- `echo` 명령으로는 값이 보임

**원인:**

- 시스템 환경 변수를 설정했지만 터미널을 재시작하지 않음
- IDE에서 실행 시 환경 변수가 전달되지 않음

**해결 방법:**

#### 방법 1: 터미널 재시작

시스템 환경 변수를 변경한 경우:

1. 모든 터미널/cmd/PowerShell 창 닫기
2. 새로 열기
3. 환경 변수 확인
4. 애플리케이션 실행

#### 방법 2: 동일한 터미널 세션에서 설정 및 실행

```cmd
# 같은 터미널 세션에서 실행
set HEDERA_ACCOUNT_ID=0.0.1234567
set HEDERA_PRIVATE_KEY=302e020100...
.\mvnw.cmd spring-boot:run
```

#### 방법 3: 배치 파일 사용

`run.cmd` 파일 생성:

```cmd
@echo off
set HEDERA_ACCOUNT_ID=0.0.1234567
set HEDERA_PRIVATE_KEY=302e020100...
mvnw.cmd spring-boot:run
```

실행:

```cmd
run.cmd
```

---

### 문제 2.3: Private Key 형식 오류

**증상:**

```
Exception in thread "main" java.lang.IllegalArgumentException: invalid private key encoding
```

**원인:**

- Private Key 형식이 올바르지 않음
- 공백이나 줄바꿈이 포함됨
- Base64 형식 사용 (지원 안 함)

**해결 방법:**

#### 1. DER 형식 확인

올바른 형식:

```
302e020100300506032b657004220420[64자 hex]
```

#### 2. 공백 및 줄바꿈 제거

Private Key에 공백이나 줄바꿈이 없는지 확인:

```cmd
# ❌ 잘못된 예
set HEDERA_PRIVATE_KEY=302e020100300506032b65700422042
0abcd1234...

# ✅ 올바른 예 (한 줄로)
set HEDERA_PRIVATE_KEY=302e020100300506032b657004220420abcd1234...
```

#### 3. Hedera Portal에서 Private Key 재확인

1. https://portal.hedera.com 로그인
2. Testnet → 해당 계정 선택
3. Private Key 다시 복사 (전체 선택)
4. 환경 변수 다시 설정

---

## 3. 트랜잭션 실행 문제

### 문제 3.1: "INVALID_SIGNATURE"

**증상:**

```
Status: INVALID_SIGNATURE
PrecheckStatusException: exceptional precheck status INVALID_SIGNATURE
```

**원인:**

- Private Key가 Account ID와 일치하지 않음
- Private Key가 잘못됨

**해결 방법:**

#### 1. Account ID와 Private Key 쌍 확인

Hedera Portal에서:

1. 해당 Account ID의 Private Key인지 재확인
2. 다른 계정의 Private Key를 사용하지 않았는지 확인

#### 2. 환경 변수 재설정

```cmd
# 올바른 쌍으로 설정
set HEDERA_ACCOUNT_ID=0.0.1234567
set HEDERA_PRIVATE_KEY=302e020100...  # 해당 계정의 Private Key

# 확인
echo %HEDERA_ACCOUNT_ID%
echo %HEDERA_PRIVATE_KEY%
```

#### 3. 애플리케이션 재시작

#### 4. 테스트

간단한 잔액 조회로 설정 확인:

- Account ID: 본인 계정 입력
- 조회 성공 시 설정 정상

---

### 문제 3.2: "INSUFFICIENT_ACCOUNT_BALANCE"

**증상:**

```
Status: INSUFFICIENT_ACCOUNT_BALANCE
Account balance insufficient for transaction fee
```

**원인:**

- HBAR 잔액 부족
- 트랜잭션 수수료를 낼 수 없음

**해결 방법:**

#### 1. 잔액 확인

웹 페이지에서:

- "계정 잔액 조회" 섹션
- 본인 Account ID 입력
- 조회 클릭

#### 2. Faucet에서 HBAR 받기

**Portal Faucet:**

```
https://portal.hedera.com/faucet
```

1. Account ID 입력
2. Submit 클릭
3. 1-2분 내에 HBAR 수령

**또는 Portal에서 직접:**

1. https://portal.hedera.com 로그인
2. Testnet → 해당 계정
3. "Add HBAR" 버튼 클릭

#### 3. 잔액 재확인

충분한 HBAR가 있는지 확인 (최소 1 HBAR = 100,000,000 tinybar)

---

### 문제 3.3: "INVALID_ACCOUNT_ID"

**증상:**

```
Status: INVALID_ACCOUNT_ID
The account ID is invalid or does not exist
```

**원인:**

- Account ID 형식이 잘못됨
- 존재하지 않는 계정
- 다른 네트워크의 계정 (Mainnet ID를 Testnet에서 사용)

**해결 방법:**

#### 1. Account ID 형식 확인

올바른 형식:

```
0.0.1234567
```

잘못된 형식:

```
❌ 0.1234567    (중간 .0 누락)
❌ 1234567      (접두사 누락)
❌ 0.0.1234567. (마침표 추가)
```

#### 2. 계정 존재 여부 확인

Hashscan에서 검증:

```
https://hashscan.io/testnet/account/0.0.1234567
```

계정이 존재하면 상세 정보가 표시됩니다.

#### 3. 네트워크 확인

- Testnet 계정을 사용 중인지 확인
- `application.yaml`에서 `network: testnet` 설정 확인

---

### 문제 3.4: "TRANSACTION_EXPIRED"

**증상:**

```
Status: TRANSACTION_EXPIRED
Transaction has expired
```

**원인:**

- 트랜잭션 제출이 지연됨
- 시스템 시간이 잘못됨

**해결 방법:**

#### 1. 시스템 시간 확인

```cmd
time /t
date /t
```

#### 2. 시간 동기화

Windows 시간 설정:

1. `Win + I` → "시간 및 언어"
2. "날짜 및 시간"
3. "지금 동기화" 클릭

#### 3. 재시도

트랜잭션을 다시 실행합니다.

---

## 4. 네트워크 연결 문제

### 문제 4.1: "GRPC UNAVAILABLE: io exception"

**증상:**

```
io.grpc.StatusRuntimeException: UNAVAILABLE: io exception
Channel Pipeline: [SslHandler#0, ...
```

**원인:**

- 인터넷 연결 끊김
- 방화벽이 gRPC 포트 차단
- Hedera 네트워크 일시적 장애
- VPN/Proxy 간섭

**해결 방법:**

#### 1. 인터넷 연결 확인

```cmd
ping 8.8.8.8
```

#### 2. Hedera 네트워크 상태 확인

```
https://status.hedera.com
```

#### 3. 방화벽 규칙 확인

Hedera는 다음 포트를 사용합니다:

- TCP 50211
- TCP 50212

방화벽에서 이 포트들이 허용되는지 확인하세요.

#### 4. VPN/Proxy 비활성화

VPN이나 Proxy를 사용 중이라면 일시적으로 비활성화하고 재시도하세요.

#### 5. 재시도

잠시 후 (1-2분) 다시 시도합니다.

---

### 문제 4.2: "No functional channel service provider found"

**증상:**

```
java.lang.IllegalStateException: No functional channel service provider found
```

**원인:**

- gRPC 의존성 누락
- gRPC 버전 충돌

**해결 방법:**

#### 1. Maven 종속성 재설치

```cmd
.\mvnw.cmd clean install
```

#### 2. Maven 캐시 정리

```cmd
# Maven 로컬 저장소 정리
rmdir /s /q %USERPROFILE%\.m2\repository\io\grpc
.\mvnw.cmd clean install
```

#### 3. pom.xml 확인

다음 의존성이 있는지 확인:

```xml
<dependency>
    <groupId>io.grpc</groupId>
    <artifactId>grpc-okhttp</artifactId>
    <version>1.64.0</version>
</dependency>
<dependency>
    <groupId>io.grpc</groupId>
    <artifactId>grpc-stub</artifactId>
    <version>1.64.0</version>
</dependency>
```

---

## 5. Java 및 Maven 문제

### 문제 5.1: "ClassNotFoundException"

**증상:**

```
java.lang.ClassNotFoundException: com.example.hedera.HederaTimeleafApplication
```

**원인:**

- 컴파일되지 않은 클래스
- 빌드 오류

**해결 방법:**

#### 1. 클린 빌드

```cmd
.\mvnw.cmd clean package
```

#### 2. 컴파일 에러 확인

빌드 출력에서 `[ERROR]` 메시지 확인

#### 3. target 디렉토리 삭제 후 재빌드

```cmd
rmdir /s /q target
.\mvnw.cmd spring-boot:run
```

---

### 문제 5.2: "OutOfMemoryError: Java heap space"

**증상:**

```
java.lang.OutOfMemoryError: Java heap space
```

**원인:**

- Maven에 할당된 메모리 부족

**해결 방법:**

#### 1. MAVEN_OPTS 설정

```cmd
set MAVEN_OPTS=-Xmx1024m -XX:MaxPermSize=256m
.\mvnw.cmd spring-boot:run
```

#### 2. 영구 설정 (시스템 환경 변수)

`MAVEN_OPTS` 환경 변수 추가:

- 변수 이름: `MAVEN_OPTS`
- 변수 값: `-Xmx1024m -XX:MaxPermSize=256m`

---

## 6. Thymeleaf 템플릿 문제

### 문제 6.1: "Neither BindingResult nor plain target object"

**증상:**

```
org.thymeleaf.exceptions.TemplateProcessingException: Neither BindingResult nor plain target object for bean name 'transferForm' available as request attribute
```

**원인:**

- POST 요청 후 Model에 폼 객체가 없음

**해결 방법:**

이 문제는 이미 수정되었습니다 (`addAllFormAttributes` 메서드).
최신 코드를 사용하고 있는지 확인하세요.

---

### 문제 6.2: "Template might not exist"

**증상:**

```
org.thymeleaf.exceptions.TemplateInputException: Error resolving template [index], template might not exist
```

**원인:**

- `index.html` 파일이 없거나 위치가 잘못됨

**해결 방법:**

#### 1. 파일 위치 확인

```
src/main/resources/templates/index.html
```

#### 2. 파일 존재 여부 확인

```cmd
dir src\main\resources\templates\index.html
```

#### 3. 빌드 재실행

```cmd
.\mvnw.cmd clean package spring-boot:run
```

---

## 7. 일반적인 팁

### 디버깅 로그 활성화

더 자세한 로그를 보려면 `application.yaml`에 추가:

```yaml
logging:
  level:
    com.example.hedera: DEBUG
    com.hedera.hashgraph: DEBUG
```

### 트랜잭션 검증

모든 트랜잭션은 Hashscan에서 확인 가능:

```
https://hashscan.io/testnet/transaction/[Transaction-ID]
```

### 캐시 정리

문제가 지속되면:

```cmd
# 1. Maven 캐시 정리
rmdir /s /q %USERPROFILE%\.m2\repository

# 2. 프로젝트 재빌드
.\mvnw.cmd clean install

# 3. 애플리케이션 실행
.\mvnw.cmd spring-boot:run
```

---

## 📞 추가 지원

위의 해결 방법으로 문제가 해결되지 않으면:

1. **Hedera Discord**: https://hedera.com/discord

   - #support 채널에서 질문

2. **Stack Overflow**:

   - 태그: `hedera-hashgraph`, `hedera`

3. **GitHub Issues**:

   - 프로젝트 저장소에 이슈 등록

4. **Hedera 문서**:
   - https://docs.hedera.com

---

**도움이 되었나요?** 추가 문제가 발생하면 이 가이드를 업데이트하세요!
