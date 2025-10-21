# Hedera Operator 설정 완벽 가이드

이 가이드는 Hedera Testnet 계정(Operator)을 생성하고 설정하는 모든 과정을 단계별로 설명합니다.

## 📑 목차

1. [Hedera Portal을 통한 계정 생성](#1-hedera-portal을-통한-계정-생성)
2. [HashPack Wallet을 통한 계정 생성](#2-hashpack-wallet을-통한-계정-생성)
3. [환경 변수 설정 방법](#3-환경-변수-설정-방법)
4. [Private Key 관리 및 보안](#4-private-key-관리-및-보안)
5. [테스트 및 검증](#5-테스트-및-검증)

---

## 1. Hedera Portal을 통한 계정 생성

Hedera Portal은 가장 간단하고 빠른 Testnet 계정 생성 방법입니다.

### 1.1 Hedera Portal 회원가입

#### 단계 1: Portal 접속

```
https://portal.hedera.com
```

#### 단계 2: 회원가입

1. 우측 상단 **"Sign Up"** 버튼 클릭
2. 필수 정보 입력:
   - Email 주소
   - 비밀번호 (최소 8자, 대소문자 + 숫자 + 특수문자)
   - 이름 (선택)
3. 이용약관 동의
4. **"Create Account"** 클릭

#### 단계 3: 이메일 인증

1. 등록한 이메일 확인
2. Hedera에서 보낸 인증 메일 열기
3. **"Verify Email"** 링크 클릭
4. 자동으로 로그인됨

### 1.2 Testnet 계정 생성

#### 단계 1: Testnet 메뉴 접속

1. 로그인 후 대시보드에서 좌측 메뉴 확인
2. **"Testnet"** 메뉴 클릭

#### 단계 2: 계정 생성

1. **"Create Testnet Account"** 버튼 클릭
2. 몇 초 후 자동으로 계정 생성 완료

#### 단계 3: 계정 정보 확인 및 저장

화면에 다음 정보가 표시됩니다:

```
Account ID: 0.0.1234567
Public Key: 302a300506032b6570032100[64자리 hex]
Private Key: 302e020100300506032b657004220420[64자리 hex]
Balance: 1000 HBAR
```

**⚠️ 중요: 다음 정보를 안전하게 저장하세요!**

| 항목            | 설명                | 용도                               |
| --------------- | ------------------- | ---------------------------------- |
| Account ID      | `0.0.xxxxxx` 형식   | 계정 식별자 (공개 가능)            |
| Public Key      | 302a로 시작하는 hex | 트랜잭션 검증용 (공개 가능)        |
| **Private Key** | 302e로 시작하는 hex | **트랜잭션 서명용 (절대 비공개!)** |

**저장 방법:**

1. 텍스트 파일로 저장 (예: `hedera-testnet-account.txt`)
2. 비밀번호 관리자에 저장 (1Password, LastPass 등)
3. 안전한 암호화된 저장소 사용

```text
# 저장 예시 (hedera-testnet-account.txt)
=================================
Hedera Testnet Account
Created: 2025-10-21
=================================

Account ID:
0.0.1234567

Public Key:
302a300506032b6570032100abcd1234...

Private Key (⚠️ KEEP SECRET):
302e020100300506032b657004220420abcd1234...

Network: testnet
Initial Balance: 1000 HBAR
=================================
```

#### 단계 4: 추가 HBAR 받기 (필요시)

Portal에서는 기본적으로 1,000 HBAR를 제공하지만, 더 필요한 경우:

1. Testnet 계정 페이지에서 **"Add HBAR"** 버튼 클릭
2. 또는 Faucet 사용:
   ```
   https://portal.hedera.com/faucet
   ```
3. Account ID 입력
4. **"Submit"** 클릭
5. 몇 초 내에 추가 HBAR 수령 (보통 100-1000 HBAR)

---

## 2. HashPack Wallet을 통한 계정 생성

HashPack은 Hedera의 공식 지갑 중 하나로, 브라우저 확장 프로그램 형태입니다.

### 2.1 HashPack 설치

#### 단계 1: 지원 브라우저 확인

- ✅ Chrome
- ✅ Brave
- ✅ Edge
- ✅ Firefox (베타)

#### 단계 2: 확장 프로그램 설치

1. 브라우저에서 접속:
   ```
   https://www.hashpack.app
   ```
2. **"Download"** 버튼 클릭
3. 브라우저 확장 프로그램 스토어로 이동
4. **"Add to [Browser]"** 클릭
5. 설치 완료 후 브라우저 툴바에 HashPack 아이콘 표시

### 2.2 지갑 생성

#### 단계 1: 초기 설정

1. HashPack 아이콘 클릭
2. **"Create New Wallet"** 선택

#### 단계 2: 시드 구문 백업 (매우 중요!)

화면에 **12개 또는 24개의 단어**가 표시됩니다:

```
예시 시드 구문 (12단어):
abandon ability able about above absent absorb abstract absurd abuse access accident
```

**⚠️ 매우 중요:**

- 이 단어들은 지갑 복구를 위한 **유일한 방법**입니다
- 순서대로 정확하게 기록하세요
- 절대 디지털로 저장하지 마세요 (해킹 위험)
- 종이에 적어서 안전한 곳에 보관하세요

**권장 백업 방법:**

1. 종이에 2-3부 복사본 작성
2. 방화 금고나 안전 금고에 보관
3. 은행 대여 금고 이용
4. 금속 백업 플레이트 사용 (화재 방지)

#### 단계 3: 시드 구문 확인

1. 다음 화면에서 무작위 순서로 단어 선택하여 백업 확인
2. 올바르게 선택하면 다음 단계로 진행

#### 단계 4: 비밀번호 설정

1. 지갑 잠금 해제용 비밀번호 입력
2. 비밀번호 확인 재입력
3. **최소 8자, 복잡한 비밀번호 사용 권장**

#### 단계 5: 지갑 생성 완료

- 자동으로 Mainnet 계정이 생성됩니다
- 기본 Account ID 확인 가능

### 2.3 Testnet으로 전환

Testnet에서 개발하려면 네트워크를 변경해야 합니다:

#### 단계 1: 설정 메뉴 열기

1. HashPack 확장 프로그램 열기
2. 우측 상단 **⚙️ (설정)** 아이콘 클릭

#### 단계 2: 네트워크 변경

1. **"Network"** 메뉴 선택
2. **"Testnet"** 선택
3. 지갑이 자동으로 Testnet으로 전환

### 2.4 계정 정보 확인

#### Account ID 확인

1. 메인 화면에서 확인
2. 형식: `0.0.xxxxxx`
3. 클릭하여 복사 가능

#### Public Key 확인

1. 설정 → **"Account Details"**
2. Public Key 섹션에서 확인 및 복사

#### Private Key 내보내기 (⚠️ 주의 필요)

**방법 1: DER 형식 (권장)**

1. 설정 → **"Export Private Key"**
2. 지갑 비밀번호 입력
3. **"DER Encoded"** 형식 선택
4. Private Key 복사
5. 형식: `302e020100300506032b657004220420[64자]`

**방법 2: Raw 형식**

1. 설정 → **"Export Private Key"**
2. 지갑 비밀번호 입력
3. **"Raw"** 형식 선택
4. Private Key 복사
5. 형식: `[64자 hex]`

**⚠️ 보안 경고:**

- Private Key 내보내기는 매우 위험한 작업입니다
- 화면을 녹화하거나 스크린샷을 찍지 마세요
- 공공 장소나 공유 컴퓨터에서 하지 마세요
- 복사 후 클립보드를 즉시 지우세요
- Private Key를 안전한 곳에만 저장하세요

### 2.5 테스트용 HBAR 받기

Testnet 계정은 기본적으로 잔액이 0입니다. Faucet에서 무료 HBAR를 받으세요:

#### 방법 1: Portal Faucet

```
https://portal.hedera.com/faucet
```

1. Account ID 입력
2. **"Submit"** 클릭
3. 1-2분 내에 100-1000 HBAR 수령

#### 방법 2: Community Faucet

일부 커뮤니티에서 운영하는 Faucet도 있습니다:

- Discord의 Hedera 봇
- Telegram 봇

---

## 3. 환경 변수 설정 방법

계정 정보를 얻은 후, 애플리케이션에서 사용하기 위해 환경 변수로 설정합니다.

### 3.1 Windows Command Prompt (cmd)

#### 임시 설정 (현재 터미널 세션만)

```cmd
# 환경 변수 설정
set HEDERA_ACCOUNT_ID=0.0.1234567
set HEDERA_PRIVATE_KEY=302e020100300506032b657004220420abcd1234...

# 확인
echo %HEDERA_ACCOUNT_ID%
echo %HEDERA_PRIVATE_KEY%

# 애플리케이션 실행
cd d:\local_project\hedera-timeleaf
.\mvnw.cmd spring-boot:run
```

**장점:**

- 빠르고 간단
- 테스트용으로 적합

**단점:**

- 터미널을 닫으면 사라짐
- 매번 다시 설정 필요

#### 배치 파일 사용

환경 변수 설정을 자동화하려면 배치 파일을 만드세요:

**파일 생성: `run-with-env.cmd`**

```cmd
@echo off
set HEDERA_ACCOUNT_ID=0.0.1234567
set HEDERA_PRIVATE_KEY=302e020100300506032b657004220420abcd1234...

echo Starting Hedera Timeleaf with Operator credentials...
echo Account ID: %HEDERA_ACCOUNT_ID%
echo.

mvnw.cmd spring-boot:run
```

**사용:**

```cmd
cd d:\local_project\hedera-timeleaf
run-with-env.cmd
```

**⚠️ 보안:**

- 이 파일을 Git에 커밋하지 마세요!
- `.gitignore`에 추가:
  ```
  run-with-env.cmd
  ```

### 3.2 Windows PowerShell

#### 임시 설정 (현재 세션만)

```powershell
# 환경 변수 설정
$env:HEDERA_ACCOUNT_ID = "0.0.1234567"
$env:HEDERA_PRIVATE_KEY = "302e020100300506032b657004220420abcd1234..."

# 확인
Write-Host "Account ID: $env:HEDERA_ACCOUNT_ID"
Write-Host "Private Key: $env:HEDERA_PRIVATE_KEY"

# 애플리케이션 실행
cd d:\local_project\hedera-timeleaf
.\mvnw.cmd spring-boot:run
```

#### PowerShell 스크립트 사용

**파일 생성: `run-with-env.ps1`**

```powershell
# Hedera Timeleaf 실행 스크립트
$env:HEDERA_ACCOUNT_ID = "0.0.1234567"
$env:HEDERA_PRIVATE_KEY = "302e020100300506032b657004220420abcd1234..."

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Hedera Timeleaf Application" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Account ID: $env:HEDERA_ACCOUNT_ID" -ForegroundColor Green
Write-Host "Network: Testnet" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# 애플리케이션 실행
.\mvnw.cmd spring-boot:run
```

**실행 정책 설정 (최초 1회):**

```powershell
# 관리자 권한으로 PowerShell 실행
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
```

**사용:**

```powershell
cd d:\local_project\hedera-timeleaf
.\run-with-env.ps1
```

### 3.3 시스템 환경 변수 (영구 설정)

Windows에서 영구적으로 환경 변수를 설정하려면:

#### 방법 1: GUI 사용

**단계 1: 시스템 속성 열기**

- `Win + R` → `sysdm.cpl` 입력 → Enter

**단계 2: 환경 변수 설정**

1. **"고급"** 탭 선택
2. **"환경 변수"** 버튼 클릭
3. **"사용자 변수"** 섹션에서 **"새로 만들기"** 클릭

**단계 3: HEDERA_ACCOUNT_ID 추가**

- 변수 이름: `HEDERA_ACCOUNT_ID`
- 변수 값: `0.0.1234567`
- **"확인"** 클릭

**단계 4: HEDERA_PRIVATE_KEY 추가**

- 변수 이름: `HEDERA_PRIVATE_KEY`
- 변수 값: `302e020100300506032b657004220420abcd1234...`
- **"확인"** 클릭

**단계 5: 적용**

- 모든 창에서 **"확인"** 클릭
- **모든 터미널 창을 닫고 다시 열기**

**장점:**

- 한 번만 설정하면 영구 적용
- 모든 터미널에서 자동 적용

**단점:**

- 시스템 전체에 노출 (보안 위험)
- 여러 Testnet 계정 사용 시 불편

#### 방법 2: PowerShell 명령어 사용

```powershell
# 사용자 환경 변수 설정 (영구)
[Environment]::SetEnvironmentVariable("HEDERA_ACCOUNT_ID", "0.0.1234567", "User")
[Environment]::SetEnvironmentVariable("HEDERA_PRIVATE_KEY", "302e020100300506032b657004220420abcd1234...", "User")

# 확인 (새 터미널에서)
$env:HEDERA_ACCOUNT_ID
$env:HEDERA_PRIVATE_KEY
```

**삭제:**

```powershell
[Environment]::SetEnvironmentVariable("HEDERA_ACCOUNT_ID", $null, "User")
[Environment]::SetEnvironmentVariable("HEDERA_PRIVATE_KEY", $null, "User")
```

### 3.4 IDE 설정

#### IntelliJ IDEA

**방법 1: Run Configuration**

1. `Run` → `Edit Configurations...`
2. Spring Boot 실행 구성 선택 (또는 새로 생성)
3. `Environment variables` 필드에 입력:
   ```
   HEDERA_ACCOUNT_ID=0.0.1234567;HEDERA_PRIVATE_KEY=302e020100...
   ```
4. `Apply` → `OK`

**방법 2: EnvFile 플러그인 사용**

1. `File` → `Settings` → `Plugins`
2. "EnvFile" 검색 및 설치
3. 프로젝트 루트에 `.env` 파일 생성:
   ```env
   HEDERA_ACCOUNT_ID=0.0.1234567
   HEDERA_PRIVATE_KEY=302e020100300506032b657004220420abcd1234...
   ```
4. Run Configuration에서 `.env` 파일 지정
5. `.gitignore`에 추가:
   ```
   .env
   ```

#### VS Code

**방법 1: launch.json 설정**

`.vscode/launch.json` 파일 생성:

```json
{
  "version": "0.2.0",
  "configurations": [
    {
      "type": "java",
      "name": "Spring Boot - HederaTimeleafApplication",
      "request": "launch",
      "mainClass": "com.example.hedera.HederaTimeleafApplication",
      "projectName": "hedera-timeleaf",
      "env": {
        "HEDERA_ACCOUNT_ID": "0.0.1234567",
        "HEDERA_PRIVATE_KEY": "302e020100300506032b657004220420abcd1234..."
      }
    }
  ]
}
```

**방법 2: .env 파일 사용**

1. `.env` 파일 생성:

   ```env
   HEDERA_ACCOUNT_ID=0.0.1234567
   HEDERA_PRIVATE_KEY=302e020100300506032b657004220420abcd1234...
   ```

2. `settings.json`에 추가:

   ```json
   {
     "java.configuration.runtimes": [
       {
         "name": "JavaSE-20",
         "path": "C:\\Program Files\\Java\\jdk-20",
         "default": true
       }
     ]
   }
   ```

3. `.gitignore`에 추가:
   ```
   .env
   ```

#### Eclipse

**Run Configurations 설정:**

1. `Run` → `Run Configurations...`
2. `Spring Boot App` → 해당 프로젝트 선택
3. `Environment` 탭 선택
4. `New` 버튼으로 환경 변수 추가:
   - Name: `HEDERA_ACCOUNT_ID`, Value: `0.0.1234567`
   - Name: `HEDERA_PRIVATE_KEY`, Value: `302e020100...`
5. `Apply` → `Run`

### 3.5 프로젝트 내 설정 파일 사용 (가장 권장 ⭐)

가장 편리하고 안전한 방법입니다. 프로젝트 내에서 설정을 관리합니다.

#### 단계 1: application-local.yaml 파일 생성

**Windows Command Prompt:**

```cmd
# 예시 파일을 복사하여 실제 설정 파일 생성
copy src\main\resources\application-local.yaml.example src\main\resources\application-local.yaml
```

**Windows PowerShell:**

```powershell
Copy-Item src\main\resources\application-local.yaml.example src\main\resources\application-local.yaml
```

#### 단계 2: 설정 파일 편집

텍스트 에디터로 `src\main\resources\application-local.yaml` 열기:

```cmd
notepad src\main\resources\application-local.yaml
```

다음 부분을 본인의 정보로 수정:

```yaml
app:
  hedera:
    # 본인의 Account ID로 변경
    account-id: 0.0.1234567

    # 본인의 Private Key로 변경
    private-key: 302e020100300506032b657004220420abcd1234...
```

#### 단계 3: 저장 및 실행

파일을 저장하고 애플리케이션을 실행하면 자동으로 적용됩니다:

```cmd
.\mvnw.cmd spring-boot:run
```

**장점:**

- ✅ 환경 변수 설정 불필요
- ✅ IDE에서 실행 시에도 자동 적용
- ✅ 터미널 재시작 불필요
- ✅ Git에 자동으로 제외됨 (.gitignore에 포함)
- ✅ 프로젝트별로 다른 계정 사용 가능

**주의:**

- ⚠️ `application-local.yaml` 파일은 절대 Git에 커밋하지 마세요
- ⚠️ 이미 `.gitignore`에 추가되어 있습니다

#### 설정 우선순위

Spring Boot는 다음 순서로 설정을 적용합니다 (높은 것이 우선):

1. **환경 변수** (HEDERA_ACCOUNT_ID, HEDERA_PRIVATE_KEY)
2. **application-local.yaml** (로컬 개발용)
3. **application.yaml** (기본 설정)

따라서 환경 변수가 설정되어 있으면 application-local.yaml보다 우선됩니다.

### 3.6 .env 파일 사용 (대안)

보안과 편의성을 모두 고려한 또 다른 방법입니다.

#### 단계 1: .env 파일 생성

프로젝트 루트에 `.env` 파일 생성:

```env
# Hedera Testnet Operator Credentials
HEDERA_ACCOUNT_ID=0.0.1234567
HEDERA_PRIVATE_KEY=302e020100300506032b657004220420abcd1234...
```

#### 단계 2: .gitignore에 추가

`.gitignore` 파일에 다음 추가:

```gitignore
# Environment variables (contains secrets)
.env
.env.local
.env.*.local

# Windows scripts with credentials
run-with-env.cmd
run-with-env.ps1
```

#### 단계 3: .env.example 생성

다른 개발자를 위해 예시 파일 생성:

```env
# .env.example
# Copy this file to .env and fill in your credentials

# Hedera Testnet Account ID (format: 0.0.xxxxx)
HEDERA_ACCOUNT_ID=0.0.YOUR_ACCOUNT_ID

# Hedera Private Key (DER encoded format)
HEDERA_PRIVATE_KEY=YOUR_PRIVATE_KEY_HERE
```

이 파일은 Git에 커밋해도 안전합니다.

---

## 4. Private Key 관리 및 보안

Private Key는 Hedera 계정의 완전한 통제권을 제공하므로 매우 중요합니다.

### 4.1 Private Key 형식

Hedera SDK는 여러 형식의 Private Key를 지원합니다:

#### DER 인코딩 형식 (권장)

```
302e020100300506032b657004220420[64자리 hex]
```

- 총 길이: 96자 (hex)
- `302e020100300506032b65700422042​0`로 시작
- Hedera Portal에서 제공하는 기본 형식
- 가장 안전하고 표준적인 형식

#### Raw 형식

```
[64자리 hex]
```

- 총 길이: 64자 (hex)
- Private key의 원시 바이트 값
- 일부 지갑에서 사용

#### Base64 인코딩 형식 (지원 안 함)

```
MC4CAQAwBQYDK2VwBCIEI...
```

- ⚠️ 이 프로젝트에서는 사용 불가
- Hex 형식으로 변환 필요

### 4.2 Private Key 검증

Private Key가 올바른 형식인지 확인하는 방법:

#### 온라인 도구 (⚠️ 보안 위험)

**절대 실제 Private Key로 하지 마세요! 테스트 계정만 사용하세요.**

#### Java 코드로 검증

```java
import com.hedera.hashgraph.sdk.PrivateKey;

public class ValidateKey {
    public static void main(String[] args) {
        String privateKeyStr = "302e020100300506032b657004220420...";

        try {
            PrivateKey privateKey = PrivateKey.fromString(privateKeyStr);
            System.out.println("✅ Valid private key");
            System.out.println("Public Key: " + privateKey.getPublicKey());
        } catch (Exception e) {
            System.out.println("❌ Invalid private key: " + e.getMessage());
        }
    }
}
```

#### 명령줄에서 검증 (애플리케이션 사용)

```powershell
# 환경 변수 설정
$env:HEDERA_ACCOUNT_ID = "0.0.1234567"
$env:HEDERA_PRIVATE_KEY = "302e020100..."

# 애플리케이션 실행 후 로그 확인
.\mvnw.cmd spring-boot:run
```

로그에서 확인:

```
✅ Operator detected: true
✅ Account ID: 0.0.1234567
```

### 4.3 보안 모범 사례

#### ❌ 하지 말아야 할 것

1. **Git에 커밋**

   ```java
   // ❌ 절대 하지 마세요!
   private static final String PRIVATE_KEY = "302e020100...";
   ```

2. **코드에 하드코딩**

   ```yaml
   # ❌ application.yaml에 직접 입력
   hedera:
     account-id: 0.0.1234567
     private-key: 302e020100...
   ```

3. **로그에 출력**

   ```java
   // ❌ Private key가 로그에 남음
   logger.info("Private key: " + privateKey);
   ```

4. **공개 저장소에 업로드**

   - GitHub, GitLab 등 공개 저장소
   - 실수로 올려도 즉시 발견되어 악용됨

5. **스크린샷 공유**

   - 포럼, Discord 등에 에러 메시지 공유 시 주의
   - Private key가 포함될 수 있음

6. **클립보드에 오래 보관**
   - 복사 후 즉시 붙여넣고 클립보드 지우기

#### ✅ 해야 할 것

1. **환경 변수 사용**

   ```java
   String privateKey = System.getenv("HEDERA_PRIVATE_KEY");
   ```

2. **.gitignore 설정**

   ```gitignore
   .env
   .env.local
   run-with-env.*
   *-credentials.*
   ```

3. **비밀번호 관리자 사용**

   - 1Password
   - LastPass
   - Bitwarden
   - KeePass

4. **권한 제한**

   - Testnet: 테스트용으로만 사용
   - Mainnet: 최소 권한 원칙 적용

5. **정기적인 키 로테이션**

   - 3-6개월마다 새 계정 생성
   - 이전 계정 비활성화

6. **백업 암호화**
   - Private key 백업 파일을 암호화
   - 7-Zip, VeraCrypt 등 사용

### 4.4 손상된 경우 대처 방법

Private Key가 노출되었다고 의심되면:

#### Testnet 계정

1. **즉시 자산 이전**
   - 모든 HBAR, 토큰을 새 계정으로 이전
2. **새 계정 생성**
   - Hedera Portal에서 새 Testnet 계정 생성
3. **환경 변수 업데이트**
   - 새 Account ID와 Private Key로 변경

#### Mainnet 계정 (실제 자산)

1. **긴급 자산 이전**
   - 즉시 모든 자산을 안전한 계정으로 이전
2. **계정 동결 (가능한 경우)**
   - 토큰의 Freeze Key로 계정 동결
3. **Hedera 지원팀 연락**

   - 공식 Discord 또는 지원 채널 이용

4. **보안 감사**
   - 시스템 전체 보안 점검
   - 멀웨어 검사

---

## 5. 테스트 및 검증

설정이 완료되면 제대로 작동하는지 테스트합니다.

### 5.1 환경 변수 확인

#### Command Prompt

```cmd
echo %HEDERA_ACCOUNT_ID%
echo %HEDERA_PRIVATE_KEY%
```

예상 출력:

```
0.0.1234567
302e020100300506032b657004220420abcd1234...
```

#### PowerShell

```powershell
Write-Host "Account ID: $env:HEDERA_ACCOUNT_ID"
Write-Host "Private Key: $env:HEDERA_PRIVATE_KEY"
```

출력이 비어있으면:

- 환경 변수가 설정되지 않음
- 터미널을 재시작해야 함 (시스템 환경 변수 변경 시)

### 5.2 애플리케이션 시작

```cmd
cd d:\local_project\hedera-timeleaf
.\mvnw.cmd spring-boot:run
```

성공적인 시작 로그:

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

Started HederaTimeleafApplication in 1.275 seconds
Tomcat started on port 8080 (http)
```

### 5.3 웹 인터페이스 테스트

#### 1. 브라우저 접속

```
http://localhost:8080
```

#### 2. Operator 상태 확인

페이지 상단에 다음 배지가 표시되어야 합니다:

**Operator 설정됨:**

```
🟢 Operator: Configured
```

**Operator 미설정:**

```
🔴 Operator: Not Configured
```

#### 3. 계정 잔액 조회 테스트 (Operator 불필요)

1. "계정 잔액 조회" 섹션에서:

   - Account ID 입력: `0.0.3` (Hedera Testnet 공개 계정)
   - "조회" 버튼 클릭

2. 예상 결과:
   ```
   0.0.3 의 hbars: 1632321.91740972 ℏ, tokens: {}
   ```

✅ 성공하면 기본 연결이 작동하는 것입니다.

#### 4. 본인 계정 잔액 조회

1. Account ID 입력: `0.0.1234567` (본인 계정)
2. "조회" 버튼 클릭
3. 예상 결과:
   ```
   0.0.1234567 의 hbars: 1000.0 ℏ, tokens: {}
   ```

#### 5. HBAR 전송 테스트 (Operator 필요)

**⚠️ 주의: 실제 Testnet HBAR를 전송합니다.**

1. "HBAR 전송" 섹션에서:

   - To Account ID: `0.0.3` (또는 다른 테스트 계정)
   - Amount (tinybar): `100000000` (= 1 HBAR)
   - "전송" 버튼 클릭

2. 예상 결과:

   ```
   Transfer completed! Transaction ID: 0.0.1234567@1729486800.123456789
   ```

3. 검증:
   - [Hashscan Testnet](https://hashscan.io/testnet) 접속
   - Transaction ID로 검색
   - 트랜잭션 상태 확인: `SUCCESS`

### 5.4 일반적인 오류 및 해결

#### 오류 1: INVALID_SIGNATURE

```
Status: INVALID_SIGNATURE
```

**원인:**

- Private Key가 잘못됨
- Private Key 형식이 올바르지 않음

**해결:**

1. Private Key 형식 확인 (DER 인코딩)
2. 환경 변수 다시 설정
3. 공백이나 줄바꿈 제거

#### 오류 2: INVALID_ACCOUNT_ID

```
Status: INVALID_ACCOUNT_ID
```

**원인:**

- Account ID 형식이 잘못됨
- 존재하지 않는 계정

**해결:**

1. Account ID 형식 확인: `0.0.xxxxx`
2. Hedera Portal에서 계정 ID 재확인
3. 네트워크 확인 (Testnet vs Mainnet)

#### 오류 3: INSUFFICIENT_ACCOUNT_BALANCE

```
Status: INSUFFICIENT_ACCOUNT_BALANCE
```

**원인:**

- HBAR 잔액 부족
- 트랜잭션 수수료를 낼 수 없음

**해결:**

1. Faucet에서 HBAR 받기:
   ```
   https://portal.hedera.com/faucet
   ```
2. 잔액 확인:
   - "계정 잔액 조회"로 본인 계정 확인

#### 오류 4: Operator Not Configured

```
❌ Operator credentials not configured. Please set HEDERA_ACCOUNT_ID and HEDERA_PRIVATE_KEY.
```

**원인:**

- 환경 변수가 설정되지 않음
- 환경 변수 이름이 틀림

**해결:**

1. 환경 변수 확인:
   ```cmd
   echo %HEDERA_ACCOUNT_ID%
   echo %HEDERA_PRIVATE_KEY%
   ```
2. 올바르게 설정:
   ```cmd
   set HEDERA_ACCOUNT_ID=0.0.1234567
   set HEDERA_PRIVATE_KEY=302e020100...
   ```
3. 애플리케이션 재시작

#### 오류 5: Connection Timeout

```
GRPC UNAVAILABLE: io exception
```

**원인:**

- 네트워크 연결 문제
- 방화벽이 gRPC 포트 차단
- Hedera 네트워크 일시적 장애

**해결:**

1. 인터넷 연결 확인
2. 방화벽 설정 확인 (포트 50211, 50212)
3. 잠시 후 재시도
4. Hedera 상태 확인:
   ```
   https://status.hedera.com
   ```

### 5.5 성공적인 설정 체크리스트

모든 항목을 확인하세요:

- ✅ Hedera Testnet 계정 생성 완료
- ✅ Account ID 저장: `0.0.xxxxxx`
- ✅ Private Key 안전하게 저장 (DER 형식)
- ✅ 환경 변수 설정 완료
- ✅ 환경 변수 확인 (`echo` 명령으로 검증)
- ✅ 애플리케이션 정상 시작
- ✅ http://localhost:8080 접속 가능
- ✅ "Operator: Configured" 배지 표시
- ✅ 계정 잔액 조회 성공 (본인 계정)
- ✅ HBAR 전송 테스트 성공
- ✅ Transaction ID로 Hashscan에서 검증 완료
- ✅ Private Key를 Git에 커밋하지 않음 (`.gitignore` 설정)

모든 항목이 체크되면 설정이 완료되었습니다! 🎉

---

## 📞 추가 지원

설정 중 문제가 발생하면:

1. **README.md 문제 해결 섹션** 확인
2. **Hedera 공식 문서** 참조:
   - https://docs.hedera.com
3. **커뮤니티 질문**:
   - Discord: https://hedera.com/discord
   - Stack Overflow: 태그 `hedera-hashgraph`
4. **Portal 지원**:
   - portal.hedera.com의 Help 메뉴

---

**작성일**: 2025-10-21
**버전**: 1.0
**대상**: Hedera Testnet
