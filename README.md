# Hedera + Thymeleaf 데모

Hedera 네트워크의 주요 기능을 테스트할 수 있는 Spring Boot + Thymeleaf 웹 애플리케이션입니다.

## 📚 문서 가이드

- **🚀 [빠른 시작 가이드](QUICKSTART.md)** - 5분 안에 시작하기
- **⚙️ [프로젝트 내 환경 설정](PROJECT_CONFIG_GUIDE.md)** - 프로젝트 내에서 자격 증명 설정하기 (권장)
- **📖 [상세 설정 가이드](SETUP_GUIDE.md)** - Operator 계정 생성 및 설정 완벽 가이드
- **🔧 [문제 해결 가이드](TROUBLESHOOTING.md)** - 일반적인 문제와 해결 방법
- **📄 이 문서** - 전체 기능 및 사용법

## �📋 목차

- [기능 소개](#-기능-소개)
- [요구 사항](#-요구-사항)
- [빠른 시작](#-빠른-시작)
- [Hedera 계정 설정 가이드](#-hedera-계정-설정-가이드)
- [기능별 사용 방법](#-기능별-사용-방법)
- [프로젝트 구조](#-프로젝트-구조)
- [문제 해결](#-문제-해결)

## 🎯 기능 소개

이 애플리케이션은 Hedera Testnet에서 다음 기능을 제공합니다:

### 조회 기능 (Operator 불필요)

- ✅ **계정 잔액 조회**: 특정 계정의 HBAR 및 토큰 잔액 확인
- ✅ **계정 정보 조회**: 계정 ID, 키, 잔액, 메모 등 상세 정보 확인

### 트랜잭션 기능 (Operator 필요)

- 💰 **HBAR 전송**: 다른 계정으로 HBAR 전송
- 👤 **계정 생성**: 새로운 Hedera 계정 생성 (Private Key 자동 생성)
- 🪙 **토큰 생성**: 사용자 정의 Fungible Token 생성
- 🎨 **NFT 컬렉션 생성**: NFT 컬렉션(Non-Fungible Token) 생성
- 🖼️ **NFT 민팅**: 개별 NFT 발행 (메타데이터 포함)
- 📢 **HCS 토픽 생성**: Hedera Consensus Service 토픽 생성
- 💬 **메시지 제출**: HCS 토픽에 메시지 제출

## 🔧 요구 사항

- **Java 20+** (Java 20.0.2 권장)
- **Maven 3.9+** (Maven Wrapper 포함)
- **Hedera Testnet 계정** (트랜잭션 기능 사용 시)

## 🚀 빠른 시작

### 1. 프로젝트 클론 및 실행

```cmd
# Windows Command Prompt
cd d:\local_project\hedera-timeleaf

# Maven Wrapper를 사용하여 실행
.\mvnw.cmd spring-boot:run
```

```powershell
# Windows PowerShell
cd d:\local_project\hedera-timeleaf

# Maven Wrapper를 사용하여 실행
.\mvnw.cmd spring-boot:run
```

### 2. 브라우저에서 접속

```
http://localhost:8080
```

### 3. 기능 테스트

**Operator 없이 사용 가능한 기능:**

- 계정 잔액 조회 (예: `0.0.3` - Hedera Testnet의 공개 계정)
- 계정 정보 조회

**Operator 설정 후 사용 가능한 기능:**

- HBAR 전송, 계정 생성, 토큰/NFT 생성, HCS 등 모든 트랜잭션 기능

## 🔑 Hedera 계정 설정 가이드

트랜잭션 기능을 사용하려면 Hedera Testnet 계정(Operator)이 필요합니다.

### 방법 1: Hedera Portal에서 계정 생성 (권장)

#### 1단계: Hedera Portal 접속

```
https://portal.hedera.com
```

#### 2단계: 회원가입 및 로그인

- 이메일로 회원가입
- 로그인 후 대시보드 접속

#### 3단계: Testnet 계정 생성

1. 좌측 메뉴에서 **"Testnet"** 선택
2. **"Create Testnet Account"** 버튼 클릭
3. 자동으로 계정이 생성되며 다음 정보가 제공됩니다:
   - **Account ID**: `0.0.xxxxx` 형식
   - **Private Key**: `302e020100...` 형식 (DER 인코딩)
   - **Public Key**: 계정의 공개 키

#### 4단계: 테스트용 HBAR 받기

- Portal에서 자동으로 **1,000 HBAR**가 지급됩니다 (Testnet 전용)
- 추가로 필요한 경우 **"Add HBAR"** 버튼으로 더 받을 수 있습니다

### 방법 2: HashPack Wallet 사용

#### 1단계: HashPack 설치

```
https://www.hashpack.app
```

- Chrome/Brave/Edge 브라우저 확장 프로그램 설치

#### 2단계: 지갑 생성

1. 확장 프로그램 열기
2. **"Create New Wallet"** 선택
3. 시드 구문(Seed Phrase) 안전하게 백업
4. 비밀번호 설정

#### 3단계: Testnet으로 전환

1. 설정(⚙️) 메뉴 열기
2. **"Network"** 선택
3. **"Testnet"** 선택

#### 4단계: 계정 정보 확인

1. 메인 화면에서 **Account ID** 확인
2. 설정 → **"Export Private Key"** 에서 Private Key 확인

#### 5단계: 테스트용 HBAR 받기

```
https://portal.hedera.com/faucet
```

- Faucet에서 Account ID 입력하여 무료 HBAR 받기

### 방법 3: 애플리케이션 내에서 계정 생성

이 애플리케이션을 통해서도 새 계정을 생성할 수 있습니다:

1. **기존 Operator로 로그인** (방법 1 또는 2로 먼저 계정 생성)
2. 웹 페이지에서 **"계정 생성"** 섹션 사용
3. 초기 잔액 입력 (최소 100,000,000 tinybar = 1 HBAR)
4. 생성 버튼 클릭
5. **결과에 표시되는 Account ID와 Private Key를 안전하게 저장**

⚠️ **중요**: 생성된 Private Key는 한 번만 표시되므로 반드시 안전한 곳에 저장하세요!

### 환경 변수 설정

계정 정보를 얻은 후 다음 방법 중 하나로 설정합니다:

#### 방법 1: 프로젝트 내 설정 파일 사용 (가장 권장 ⭐)

가장 편리하고 안전한 방법입니다.

```cmd
# 1. 예시 파일을 복사하여 실제 설정 파일 생성
copy src\main\resources\application-local.yaml.example src\main\resources\application-local.yaml

# 2. 텍스트 에디터로 열기
notepad src\main\resources\application-local.yaml

# 3. 본인의 Account ID와 Private Key로 수정
#    account-id: 0.0.1234567
#    private-key: 302e020100...

# 4. 저장 후 실행
.\mvnw.cmd spring-boot:run
```

**장점:**

- ✅ 환경 변수 설정 불필요
- ✅ IDE에서도 자동 적용
- ✅ Git에 커밋되지 않음 (안전)
- ✅ 프로젝트별로 다른 계정 사용 가능

#### 방법 2: Windows Command Prompt (cmd)

```cmd
# 현재 세션에만 적용
set HEDERA_ACCOUNT_ID=0.0.xxxxx
set HEDERA_PRIVATE_KEY=302e020100300506032b657004220420...

# 애플리케이션 실행
.\mvnw.cmd spring-boot:run
```

#### Windows PowerShell

```powershell
# 현재 세션에만 적용
$env:HEDERA_ACCOUNT_ID="0.0.xxxxx"
$env:HEDERA_PRIVATE_KEY="302e020100300506032b657004220420..."

# 애플리케이션 실행
.\mvnw.cmd spring-boot:run
```

#### 영구적으로 설정 (Windows 시스템 환경 변수)

1. **Win + R** → `sysdm.cpl` 입력
2. **"고급"** 탭 → **"환경 변수"** 클릭
3. **"사용자 변수"** 또는 **"시스템 변수"**에 추가:
   - 변수 이름: `HEDERA_ACCOUNT_ID`
   - 변수 값: `0.0.xxxxx`
   - 변수 이름: `HEDERA_PRIVATE_KEY`
   - 변수 값: `302e020100...`
4. 터미널 재시작

#### IntelliJ IDEA / VS Code에서 설정

**IntelliJ IDEA:**

1. Run → Edit Configurations
2. Environment Variables에 추가:
   ```
   HEDERA_ACCOUNT_ID=0.0.xxxxx;HEDERA_PRIVATE_KEY=302e020100...
   ```

**VS Code:**

1. `.vscode/launch.json` 파일 생성 또는 수정:

```json
{
  "version": "0.2.0",
  "configurations": [
    {
      "type": "java",
      "name": "Spring Boot",
      "request": "launch",
      "mainClass": "com.example.hedera.HederaTimeleafApplication",
      "env": {
        "HEDERA_ACCOUNT_ID": "0.0.xxxxx",
        "HEDERA_PRIVATE_KEY": "302e020100..."
      }
    }
  ]
}
```

### Private Key 형식 확인

Hedera Private Key는 다음 형식이어야 합니다:

```
✅ DER 인코딩 형식 (권장):
302e020100300506032b657004220420[64자리 hex]

✅ Raw 형식 (64자 hex):
3a21034f7f3e6f8b5c4d3e2f1a0b9c8d7e6f...
```

❌ 잘못된 형식:

- Base64로 인코딩된 문자열
- 공백이나 줄바꿈 포함
- 불완전한 키 값

## 📖 기능별 사용 방법

### 1. 계정 잔액 조회 (Operator 불필요)

**입력:**

- Account ID: `0.0.3` (Hedera Testnet 공개 계정)

**결과 예시:**

```
0.0.3 의 hbars: 1632321.91740972 ℏ, tokens: {}
```

### 2. 계정 정보 조회 (Operator 불필요)

**입력:**

- Account ID: `0.0.3`

**결과 예시:**

```
계정 정보:
Account ID: 0.0.3
Key: 302a300506032b6570032100...
Balance: 1632321917409720 tinybar
Memo:
```

### 3. HBAR 전송 (Operator 필요)

**입력:**

- To Account ID: `0.0.12345` (받는 사람 계정)
- Amount (tinybar): `100000000` (= 1 HBAR)

**참고:**

- 1 HBAR = 100,000,000 tinybar
- 0.1 HBAR = 10,000,000 tinybar
- 최소 전송 금액: 1 tinybar

**결과 예시:**

```
Transfer completed! Transaction ID: 0.0.xxxxx@1234567890.123456789
```

### 4. 계정 생성 (Operator 필요)

**입력:**

- Initial Balance (tinybar): `100000000` (= 1 HBAR)

**결과 예시:**

```
New account created!
Account ID: 0.0.12346
Private Key: 302e020100300506032b657004220420...
⚠️ 이 Private Key를 안전하게 보관하세요!
```

### 5. 토큰 생성 (Operator 필요)

**입력:**

- Token Name: `MyToken`
- Token Symbol: `MTK`
- Initial Supply: `1000000`
- Decimals: `2`

**결과 예시:**

```
Token created! Token ID: 0.0.12347
Name: MyToken
Symbol: MTK
Supply: 1000000
```

### 6. NFT 컬렉션 생성 (Operator 필요)

**입력:**

- NFT Name: `MyNFTCollection`
- NFT Symbol: `MNFT`

**결과 예시:**

```
NFT created! Token ID: 0.0.12348
Name: MyNFTCollection
Symbol: MNFT
```

### 7. NFT 민팅 (Operator 필요)

**입력:**

- Token ID: `0.0.12348` (NFT 컬렉션 ID)
- Metadata: `{"name":"NFT #1","description":"First NFT","image":"ipfs://..."}`

**결과 예시:**

```
NFT minted! Serial Number: 1
Transaction ID: 0.0.xxxxx@1234567890.123456789
```

### 8. HCS 토픽 생성 (Operator 필요)

**입력:**

- Memo: `My Consensus Topic`

**결과 예시:**

```
Topic created! Topic ID: 0.0.12349
Memo: My Consensus Topic
```

### 9. 메시지 제출 (Operator 필요)

**입력:**

- Topic ID: `0.0.12349`
- Message: `Hello, Hedera Consensus Service!`

**결과 예시:**

```
Message submitted to topic 0.0.12349
Sequence Number: 1
```

## 📁 프로젝트 구조

```
hedera-timeleaf/
├── src/
│   └── main/
│       ├── java/com/example/hedera/
│       │   ├── HederaTimeleafApplication.java    # Spring Boot 메인 클래스
│       │   ├── service/
│       │   │   └── HederaService.java            # Hedera SDK 통합 서비스
│       │   └── web/
│       │       └── HomeController.java           # MVC 컨트롤러
│       └── resources/
│           ├── application.yaml                  # 애플리케이션 설정
│           └── templates/
│               └── index.html                    # Thymeleaf 템플릿
├── pom.xml                                       # Maven 의존성
├── mvnw.cmd                                      # Maven Wrapper (Windows)
└── README.md                                     # 이 파일
```

### 주요 파일 설명

#### `HederaService.java`

Hedera SDK와의 모든 상호작용을 처리하는 서비스 레이어:

- `createClient()`: Hedera Client 생성 및 네트워크 설정
- `hasOperator()`: Operator 자격 증명 확인
- `getAccountBalance()`: 계정 잔액 조회
- `getAccountInfo()`: 계정 상세 정보 조회
- `transferHbar()`: HBAR 전송
- `createAccount()`: 새 계정 생성
- `createToken()`: Fungible Token 생성
- `createNFT()`: NFT 컬렉션 생성
- `mintNFT()`: NFT 발행
- `createTopic()`: HCS 토픽 생성
- `submitMessage()`: HCS 메시지 제출

#### `HomeController.java`

웹 요청을 처리하는 Spring MVC 컨트롤러:

- 8개의 Form 레코드 클래스 (Validation 포함)
- 9개의 POST 엔드포인트 (각 기능별)
- Model 속성 관리

#### `index.html`

Thymeleaf 기반 UI:

- 반응형 그리드 레이아웃
- Operator 상태에 따른 조건부 렌더링
- Form validation 및 에러 표시
- 결과 메시지 표시

#### `application.yaml`

```yaml
server:
  port: 8080

spring:
  thymeleaf:
    cache: false # 개발 중 템플릿 캐싱 비활성화

app:
  hedera:
    network: testnet # testnet, mainnet, previewnet
```

## 🔍 문제 해결

### 포트 8080이 이미 사용 중

**증상:**

```
Web server failed to start. Port 8080 was already in use.
```

**해결 방법:**

```cmd
# 포트 사용 프로세스 확인
netstat -ano | findstr :8080

# 프로세스 종료 (PID 확인 후)
taskkill /F /PID [PID번호]

# 또는 다른 포트 사용 (application.yaml 수정)
server:
  port: 8081
```

### Operator 자격 증명 오류

**증상:**

```
INVALID_SIGNATURE or INVALID_ACCOUNT_ID
```

**해결 방법:**

1. Account ID 형식 확인: `0.0.xxxxx`
2. Private Key 형식 확인: DER 인코딩 또는 Raw 형식
3. 환경 변수가 올바르게 설정되었는지 확인:
   ```cmd
   echo %HEDERA_ACCOUNT_ID%
   echo %HEDERA_PRIVATE_KEY%
   ```
4. 애플리케이션 재시작

### 잔액 부족 오류

**증상:**

```
INSUFFICIENT_ACCOUNT_BALANCE
```

**해결 방법:**

1. Hedera Portal Faucet에서 추가 HBAR 받기:
   ```
   https://portal.hedera.com/faucet
   ```
2. Testnet 계정은 무료로 HBAR를 받을 수 있습니다

### gRPC 연결 오류

**증상:**

```
No functional channel service provider found
io.grpc.internal.AbstractManagedChannelImplBuilder ClassNotFoundException
```

**해결 방법:**

- 이 프로젝트는 이미 필요한 gRPC 의존성을 포함하고 있습니다
- Maven 캐시 정리:
  ```cmd
  .\mvnw.cmd clean
  .\mvnw.cmd install
  ```

### Java 버전 오류

**증상:**

```
Unsupported class file major version 64
```

**해결 방법:**

- Java 20 이상 설치:
  ```cmd
  java -version
  ```
- JAVA_HOME 환경 변수 확인
- pom.xml의 java.version 확인

## 📚 추가 리소스

### Hedera 공식 문서

- **Developer Portal**: https://docs.hedera.com
- **Portal (Testnet 계정)**: https://portal.hedera.com
- **Java SDK**: https://github.com/hashgraph/hedera-sdk-java
- **API 문서**: https://docs.hedera.com/hedera/sdks-and-apis

### 커뮤니티

- **Discord**: https://hedera.com/discord
- **Reddit**: https://www.reddit.com/r/Hedera
- **Stack Overflow**: Tag `hedera-hashgraph`

### 예제 코드

- **Hedera Examples**: https://github.com/hashgraph/hedera-docs/tree/main/examples

## 💡 유용한 팁

1. **Testnet vs Mainnet**

   - Testnet: 개발 및 테스트용 (무료 HBAR 제공)
   - Mainnet: 실제 운영용 (실제 HBAR 필요)

2. **Transaction Fees**

   - Testnet 트랜잭션 비용은 무료 HBAR로 충당
   - 일반적인 트랜잭션: 0.001 ~ 0.01 HBAR

3. **Rate Limits**

   - Testnet: 계정당 초당 10 트랜잭션
   - 개발 중에는 충분한 제한

4. **Private Key 보안**

   - ⚠️ Private Key를 절대 Git에 커밋하지 마세요
   - `.gitignore`에 `.env` 파일 추가
   - 환경 변수 또는 암호화된 저장소 사용

5. **디버깅**
   - Hedera Explorer에서 트랜잭션 확인:
     - Testnet: https://hashscan.io/testnet
     - Mainnet: https://hashscan.io/mainnet
   - Transaction ID로 검색하여 상태 확인

## 📞 지원

문제가 발생하거나 질문이 있으시면:

1. 이 README의 [문제 해결](#-문제-해결) 섹션 확인
2. [Hedera Discord](https://hedera.com/discord) 커뮤니티 질문
3. [Hedera Documentation](https://docs.hedera.com) 참조

---

**라이선스**: MIT
**Hedera SDK Version**: 2.39.0
**Spring Boot Version**: 3.3.5
