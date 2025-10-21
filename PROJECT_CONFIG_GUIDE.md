# 프로젝트 내 환경 설정 가이드

이 가이드는 프로젝트 내에서 Hedera Operator 자격 증명을 설정하는 방법을 설명합니다.

## 🎯 왜 이 방법을 사용하나요?

**기존 방법 (환경 변수)의 단점:**

- 터미널마다 매번 설정해야 함
- IDE에서 실행 시 별도 설정 필요
- 터미널을 닫으면 설정이 사라짐

**프로젝트 내 설정의 장점:**

- ✅ 한 번만 설정하면 끝
- ✅ 터미널, IDE 어디서나 자동 적용
- ✅ Git에 커밋되지 않아 안전
- ✅ 프로젝트별로 다른 계정 사용 가능

---

## 📝 설정 방법 (3단계)

### 1단계: 설정 파일 생성

```cmd
# Windows Command Prompt
copy src\main\resources\application-local.yaml.example src\main\resources\application-local.yaml
```

```powershell
# Windows PowerShell
Copy-Item src\main\resources\application-local.yaml.example src\main\resources\application-local.yaml
```

### 2단계: 자격 증명 입력

텍스트 에디터로 `src\main\resources\application-local.yaml` 파일을 엽니다:

```cmd
notepad src\main\resources\application-local.yaml
```

다음 부분을 본인의 정보로 수정합니다:

```yaml
app:
  hedera:
    # 본인의 Hedera Testnet Account ID
    account-id: 0.0.1234567 # ← 여기 수정

    # 본인의 Hedera Private Key (DER 인코딩 형식)
    private-key: 302e020100300506032b657004220420abcd1234... # ← 여기 수정
```

**중요:**

- Account ID는 `0.0.xxxxx` 형식
- Private Key는 `302e020100...`으로 시작하는 DER 인코딩 형식 사용
- Hedera Portal(https://portal.hedera.com)에서 확인 가능

### 3단계: 애플리케이션 실행

저장 후 애플리케이션을 실행하면 자동으로 설정이 적용됩니다:

```cmd
.\mvnw.cmd spring-boot:run
```

브라우저에서 http://localhost:8080 접속 시 "🟢 Operator: Configured" 표시를 확인하세요.

---

## 🔒 보안

### Git에서 자동 제외됨

`application-local.yaml` 파일은 이미 `.gitignore`에 포함되어 있어 Git에 커밋되지 않습니다:

```gitignore
# .gitignore
application-local.yaml
application-local.yml
src/main/resources/application-local.yaml
src/main/resources/application-local.yml
```

### 확인 방법

```cmd
# Git 상태 확인
git status

# application-local.yaml이 표시되지 않아야 함
```

---

## ⚙️ 설정 우선순위

Spring Boot는 다음 순서로 설정을 적용합니다 (높은 것이 우선):

1. **환경 변수** (HEDERA_ACCOUNT_ID, HEDERA_PRIVATE_KEY)

   - 터미널에서 `set` 또는 `$env:` 로 설정한 값

2. **application-local.yaml** (이 파일)

   - 프로젝트 내에서 설정한 값

3. **application.yaml**
   - 기본 설정 (보통 빈 값)

따라서:

- 환경 변수를 설정하면 `application-local.yaml`보다 우선 적용됩니다
- 환경 변수가 없으면 `application-local.yaml`의 값이 사용됩니다

---

## 🔄 다른 계정으로 전환

### 임시로 다른 계정 사용

환경 변수로 덮어쓰기:

```cmd
set HEDERA_ACCOUNT_ID=0.0.9999999
set HEDERA_PRIVATE_KEY=302e020100...
.\mvnw.cmd spring-boot:run
```

### 영구적으로 다른 계정 사용

`application-local.yaml` 파일 수정:

```yaml
app:
  hedera:
    account-id: 0.0.9999999 # 새 계정 ID
    private-key: 302e020100... # 새 Private Key
```

---

## 📋 예시

### 올바른 설정 예시

```yaml
app:
  hedera:
    account-id: 0.0.1234567
    private-key: 302e020100300506032b657004220420abcd1234ef5678901234567890abcdef1234567890abcdef1234567890abcd
```

### 잘못된 설정 예시

```yaml
# ❌ Account ID 형식 오류
account-id: 1234567  # 0.0. 접두사 누락

# ❌ Private Key 형식 오류
private-key: abcd1234...  # DER 인코딩 접두사 누락 (302e020100...)

# ❌ 따옴표 사용 (YAML에서는 선택사항이지만 비권장)
account-id: "0.0.1234567"
```

---

## 🆘 문제 해결

### "Operator: Not Configured" 표시

**원인:** 설정 파일이 적용되지 않음

**해결:**

1. 파일 이름 확인: `application-local.yaml` (철자 확인)
2. 파일 위치 확인: `src/main/resources/` 폴더 내
3. YAML 형식 확인: 들여쓰기가 정확한지 확인
4. 애플리케이션 재시작

### YAML 형식 오류

**증상:**

```
Error creating bean with name 'hederaService'
Could not resolve placeholder 'app.hedera.account-id'
```

**원인:** YAML 파일의 들여쓰기가 잘못됨

**해결:**
YAML은 들여쓰기가 중요합니다. 공백 2칸 또는 4칸을 일관되게 사용:

```yaml
app:
  hedera: # 공백 2칸 들여쓰기
    account-id: 0.0.1234567 # 공백 4칸 들여쓰기
    private-key: 302e020100... # 공백 4칸 들여쓰기
```

### 파일이 생성되지 않음

**원인:** 예시 파일이 없거나 복사 명령이 실패함

**해결:**

```cmd
# 파일 존재 확인
dir src\main\resources\application-local.yaml.example

# 수동으로 파일 생성
notepad src\main\resources\application-local.yaml

# 다음 내용 붙여넣기:
app:
  hedera:
    account-id: 0.0.YOUR_ACCOUNT_ID
    private-key: YOUR_PRIVATE_KEY
```

---

## 💡 팁

### IDE에서 자동 완성 사용

IntelliJ IDEA나 VS Code에서 `application-local.yaml`을 열면:

- YAML 구문 강조
- 자동 들여쓰기
- 오류 검사

### 여러 환경 관리

다른 환경(개발, 스테이징, 프로덕션)을 위해 여러 파일 사용:

- `application-local.yaml` - 로컬 개발
- `application-dev.yaml` - 개발 서버
- `application-prod.yaml` - 프로덕션 (환경 변수 권장)

실행 시 프로파일 지정:

```cmd
# dev 프로파일 사용
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev

# 또는 환경 변수로
set SPRING_PROFILES_ACTIVE=dev
.\mvnw.cmd spring-boot:run
```

### 설정 확인

애플리케이션 시작 후 로그에서 확인:

```
Started HederaTimeleafApplication in 1.275 seconds
```

브라우저에서 http://localhost:8080 접속하여 "🟢 Operator: Configured" 배지 확인

---

## 📚 관련 문서

- **빠른 시작**: `QUICKSTART.md`
- **상세 설정**: `SETUP_GUIDE.md`
- **문제 해결**: `TROUBLESHOOTING.md`
- **전체 문서**: `README.md`

---

**질문이 있으신가요?**

- Hedera Discord: https://hedera.com/discord
- Hedera 문서: https://docs.hedera.com
