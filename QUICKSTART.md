# 🚀 빠른 시작 가이드

5분 안에 Hedera 애플리케이션을 실행하세요!

## ⚡ 1단계: 계정 생성 (2분)

### Hedera Portal에서 계정 만들기

1. **Portal 접속**

   ```
   https://portal.hedera.com
   ```

2. **회원가입**

   - 이메일로 가입
   - 이메일 인증

3. **Testnet 계정 생성**
   - 좌측 메뉴 "Testnet" 클릭
   - "Create Testnet Account" 버튼
   - **Account ID**와 **Private Key** 저장! ⚠️

예시:

```
Account ID: 0.0.1234567
Private Key: 302e020100300506032b657004220420abcd1234...
```

💰 **자동으로 1,000 HBAR가 지급됩니다!**

---

## ⚙️ 2단계: 환경 변수 설정 (1분)

### 방법 1: 프로젝트 내 설정 파일 사용 (권장 ⭐)

```cmd
# 1. 예시 파일 복사
copy src\main\resources\application-local.yaml.example src\main\resources\application-local.yaml

# 2. 텍스트 에디터로 application-local.yaml 파일 열기
notepad src\main\resources\application-local.yaml

# 3. 다음 부분을 본인의 정보로 수정:
#    account-id: 0.0.YOUR_ACCOUNT_ID_HERE  →  0.0.1234567
#    private-key: YOUR_PRIVATE_KEY_HERE    →  302e020100...

# 4. 저장 후 닫기
```

**장점:**

- ✅ 터미널마다 설정할 필요 없음
- ✅ IDE에서 실행 시에도 자동 적용
- ✅ Git에 커밋되지 않음 (안전)

### 방법 2: 환경 변수 사용 (임시)

**Windows Command Prompt:**

```cmd
set HEDERA_ACCOUNT_ID=0.0.1234567
set HEDERA_PRIVATE_KEY=302e020100300506032b657004220420abcd1234...
```

**Windows PowerShell:**

```powershell
$env:HEDERA_ACCOUNT_ID="0.0.1234567"
$env:HEDERA_PRIVATE_KEY="302e020100300506032b657004220420abcd1234..."
```

⚠️ **본인의 Account ID와 Private Key로 변경하세요!**

---

## ▶️ 3단계: 애플리케이션 실행 (1분)

```cmd
cd d:\local_project\hedera-timeleaf
.\mvnw.cmd spring-boot:run
```

다음 메시지가 표시될 때까지 기다리세요:

```
Started HederaTimeleafApplication in 1.275 seconds
Tomcat started on port 8080 (http)
```

---

## 🌐 4단계: 브라우저에서 테스트 (1분)

1. **브라우저 열기**

   ```
   http://localhost:8080
   ```

2. **상태 확인**

   - 페이지 상단에 "🟢 Operator: Configured" 표시 확인

3. **첫 번째 테스트: 잔액 조회**

   - "계정 잔액 조회" 섹션
   - Account ID 입력: `0.0.3`
   - "조회" 버튼 클릭

   ✅ 결과가 표시되면 성공!

4. **두 번째 테스트: 본인 계정 조회**

   - Account ID 입력: `0.0.1234567` (본인 계정)
   - "조회" 버튼 클릭

   ✅ 잔액이 1000 HBAR로 표시되면 성공!

5. **세 번째 테스트: HBAR 전송**

   - "HBAR 전송" 섹션
   - To Account ID: `0.0.3`
   - Amount: `100000000` (= 1 HBAR)
   - "전송" 버튼 클릭

   ✅ Transaction ID가 표시되면 성공! 🎉

---

## ✅ 완료!

이제 다음 기능들을 모두 사용할 수 있습니다:

- ✅ 계정 잔액 조회
- ✅ 계정 정보 조회
- 💰 HBAR 전송
- 👤 새 계정 생성
- 🪙 토큰 생성
- 🎨 NFT 컬렉션 생성
- 🖼️ NFT 민팅
- 📢 HCS 토픽 생성
- � HCS 토픽 정보 조회
- �💬 메시지 제출

---

## 🆘 문제 발생 시

### "Operator: Not Configured" 표시

**해결:**

```cmd
# 환경 변수 확인
echo %HEDERA_ACCOUNT_ID%
echo %HEDERA_PRIVATE_KEY%

# 비어있으면 다시 설정
set HEDERA_ACCOUNT_ID=0.0.1234567
set HEDERA_PRIVATE_KEY=302e020100...

# 애플리케이션 재시작
.\mvnw.cmd spring-boot:run
```

### "Port 8080 already in use"

**해결:**

```cmd
# 포트 사용 프로세스 종료
netstat -ano | findstr :8080
taskkill /F /PID [PID번호]

# 애플리케이션 재시작
.\mvnw.cmd spring-boot:run
```

### "INVALID_SIGNATURE"

**원인:** Private Key가 틀림

**해결:**

1. Hedera Portal에서 Private Key 재확인
2. 환경 변수 다시 설정
3. 애플리케이션 재시작

### "INSUFFICIENT_ACCOUNT_BALANCE"

**원인:** HBAR 잔액 부족

**해결:**

```
https://portal.hedera.com/faucet
```

- Account ID 입력하여 추가 HBAR 받기

---

## 📚 더 알아보기

- **상세 가이드**: `SETUP_GUIDE.md` 참조
- **전체 문서**: `README.md` 참조
- **Hedera 문서**: https://docs.hedera.com
- **트랜잭션 확인**: https://hashscan.io/testnet

---

**즐거운 Hedera 개발 되세요!** 🚀
