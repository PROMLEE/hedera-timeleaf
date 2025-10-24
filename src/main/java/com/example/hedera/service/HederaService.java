package com.example.hedera.service;

import com.hedera.hashgraph.sdk.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class HederaService {

  @Value("${app.hedera.network:testnet}")
  private String network;

  // Priority: Environment variables > application-local.yaml > application.yaml
  @Value("${HEDERA_ACCOUNT_ID:${app.hedera.account-id:}}")
  private String operatorIdEnv;

  @Value("${HEDERA_PRIVATE_KEY:${app.hedera.private-key:}}")
  private String operatorKeyEnv;

  public String getNetwork() {
    return network;
  }

  public boolean hasOperator() {
    return operatorIdEnv != null && !operatorIdEnv.isBlank()
        && operatorKeyEnv != null && !operatorKeyEnv.isBlank();
  }

  public String getOperatorAccountId() {
    return hasOperator() ? operatorIdEnv : null;
  }

  public AccountInfo getOperatorInfo()
      throws PrecheckStatusException, TimeoutException {
    if (!hasOperator()) {
      return null;
    }
    return getAccountInfo(operatorIdEnv);
  }

  // 1. 계정 잔액 조회
  public AccountBalance getAccountBalance(String accountId)
      throws PrecheckStatusException, TimeoutException, ReceiptStatusException {
    try (Client client = createClient()) {
      AccountId target = AccountId.fromString(accountId);
      return new AccountBalanceQuery().setAccountId(target).execute(client);
    }
  }

  // 2. 계정 정보 조회
  public AccountInfo getAccountInfo(String accountId)
      throws PrecheckStatusException, TimeoutException {
    try (Client client = createClient()) {
      AccountId target = AccountId.fromString(accountId);
      return new AccountInfoQuery().setAccountId(target).execute(client);
    }
  }

  // 3. HBAR 전송
  public String transferHbar(String toAccountId, long amount)
      throws PrecheckStatusException, TimeoutException, ReceiptStatusException {
    if (!hasOperator()) {
      throw new IllegalStateException("운영자 계정이 설정되지 않았습니다");
    }
    try (Client client = createClient()) {
      AccountId sender = Objects.requireNonNull(client.getOperatorAccountId(), "Operator account is required");
      AccountId receiver = AccountId.fromString(toAccountId);

      TransactionResponse response = new TransferTransaction()
          .addHbarTransfer(sender, Hbar.fromTinybars(-amount))
          .addHbarTransfer(receiver, Hbar.fromTinybars(amount))
          .execute(client);

      response.getReceipt(client);
      return "전송 완료! Transaction ID: " + response.transactionId;
    }
  }

  // 4. 새 계정 생성
  public String createAccount(long initialBalance)
      throws PrecheckStatusException, TimeoutException, ReceiptStatusException {
    if (!hasOperator()) {
      throw new IllegalStateException("운영자 계정이 설정되지 않았습니다");
    }
    try (Client client = createClient()) {
      PrivateKey newAccountPrivateKey = PrivateKey.generateED25519();
      PublicKey newAccountPublicKey = newAccountPrivateKey.getPublicKey();

      TransactionResponse response = new AccountCreateTransaction()
          .setKey(newAccountPublicKey)
          .setInitialBalance(Hbar.fromTinybars(initialBalance))
          .execute(client);

      TransactionReceipt receipt = response.getReceipt(client);
      AccountId newAccountId = receipt.accountId;

      return String.format("계정 생성 완료!%nAccount ID: %s%nPublic Key: %s%nPrivate Key: %s",
          newAccountId, newAccountPublicKey, newAccountPrivateKey);
    }
  }

  // 5. 토큰 생성
  public String createToken(String name, String symbol, long initialSupply, int decimals)
      throws PrecheckStatusException, TimeoutException, ReceiptStatusException {
    if (!hasOperator()) {
      throw new IllegalStateException("운영자 계정이 설정되지 않았습니다");
    }
    try (Client client = createClient()) {
      AccountId operatorAccountId = Objects.requireNonNull(client.getOperatorAccountId(),
          "Operator account is required");
      PublicKey operatorKey = Objects.requireNonNull(client.getOperatorPublicKey(), "Operator key is required");

      TransactionResponse response = new TokenCreateTransaction()
          .setTokenName(name)
          .setTokenSymbol(symbol)
          .setDecimals(decimals)
          .setInitialSupply(initialSupply)
          .setTreasuryAccountId(operatorAccountId)
          .setAdminKey(operatorKey)
          .setSupplyKey(operatorKey)
          .execute(client);

      TransactionReceipt receipt = response.getReceipt(client);
      TokenId tokenId = receipt.tokenId;

      return String.format("토큰 생성 완료!%nToken ID: %s%n이름: %s%n심볼: %s%n발행량: %d",
          tokenId, name, symbol, initialSupply);
    }
  }

  // 6. NFT 생성 (Non-Fungible Token)
  public String createNFT(String name, String symbol)
      throws PrecheckStatusException, TimeoutException, ReceiptStatusException {
    if (!hasOperator()) {
      throw new IllegalStateException("운영자 계정이 설정되지 않았습니다");
    }
    try (Client client = createClient()) {
      AccountId operatorAccountId = Objects.requireNonNull(client.getOperatorAccountId(),
          "Operator account is required");
      PublicKey operatorKey = Objects.requireNonNull(client.getOperatorPublicKey(), "Operator key is required");

      TransactionResponse response = new TokenCreateTransaction()
          .setTokenName(name)
          .setTokenSymbol(symbol)
          .setTokenType(TokenType.NON_FUNGIBLE_UNIQUE)
          .setDecimals(0)
          .setInitialSupply(0)
          .setTreasuryAccountId(operatorAccountId)
          .setSupplyType(TokenSupplyType.FINITE)
          .setMaxSupply(100)
          .setSupplyKey(operatorKey)
          .execute(client);

      TransactionReceipt receipt = response.getReceipt(client);
      TokenId tokenId = receipt.tokenId;

      return String.format("NFT 컬렉션 생성 완료!%nToken ID: %s%n이름: %s%n심볼: %s",
          tokenId, name, symbol);
    }
  }

  // 7. NFT 민팅
  public String mintNFT(String tokenId, byte[] metadata)
      throws PrecheckStatusException, TimeoutException, ReceiptStatusException {
    if (!hasOperator()) {
      throw new IllegalStateException("운영자 계정이 설정되지 않았습니다");
    }
    try (Client client = createClient()) {
      TransactionResponse response = new TokenMintTransaction()
          .setTokenId(TokenId.fromString(tokenId))
          .setMetadata(Collections.singletonList(metadata))
          .execute(client);

      TransactionReceipt receipt = response.getReceipt(client);

      return String.format("NFT 민팅 완료!%nSerial Number: %s",
          receipt.serials.get(0));
    }
  }

  // 8. 토픽 생성 (HCS - Hedera Consensus Service)
  public String createTopic(String memo, long autoRenewPeriodSeconds)
      throws PrecheckStatusException, TimeoutException, ReceiptStatusException {
    if (!hasOperator()) {
      throw new IllegalStateException("운영자 계정이 설정되지 않았습니다");
    }
    try (Client client = createClient()) {
      long boundedAutoRenew = Math.min(Math.max(autoRenewPeriodSeconds, 1800L), 7_776_000L);

      TransactionResponse response = new TopicCreateTransaction()
          .setTopicMemo(memo)
          .setAutoRenewPeriod(Duration.ofSeconds(boundedAutoRenew))
          .execute(client);

      TransactionReceipt receipt = response.getReceipt(client);
      TopicId topicId = receipt.topicId;

      return String.format("토픽 생성 완료!%nTopic ID: %s%nMemo: %s%nAuto Renew: %d초",
          topicId, memo, boundedAutoRenew);
    }
  }

  // 9. 토픽에 메시지 전송
  public String submitMessage(String topicId, String message)
      throws PrecheckStatusException, TimeoutException, ReceiptStatusException {
    if (!hasOperator()) {
      throw new IllegalStateException("운영자 계정이 설정되지 않았습니다");
    }
    try (Client client = createClient()) {
      TransactionResponse response = new TopicMessageSubmitTransaction()
          .setTopicId(TopicId.fromString(topicId))
          .setMessage(message)
          .execute(client);

      TransactionReceipt receipt = response.getReceipt(client);

      return String.format("메시지 전송 완료!%nSequence Number: %d",
          receipt.topicSequenceNumber);
    }
  }

  public String getTopicInfo(String topicId)
      throws PrecheckStatusException, TimeoutException {
    try (Client client = createClient()) {
      TopicId target = TopicId.fromString(topicId);
      TopicInfo info = new TopicInfoQuery().setTopicId(target).execute(client);

      String memo = info.topicMemo != null && !info.topicMemo.isBlank() ? info.topicMemo : "(없음)";
      String autoRenewAccount = info.autoRenewAccountId != null ? info.autoRenewAccountId.toString() : "(없음)";
      long autoRenewSeconds = info.autoRenewPeriod != null ? info.autoRenewPeriod.getSeconds() : 0;
      String expiration = info.expirationTime != null ? info.expirationTime.toString() : "(알 수 없음)";

      return String.format(
          "토픽 정보:%nTopic ID: %s%nMemo: %s%nSequence Number: %d%nAuto Renew Account: %s%nAuto Renew Period: %d초%nExpiration Time: %s",
          info.topicId,
          memo,
          info.sequenceNumber,
          autoRenewAccount,
          autoRenewSeconds,
          expiration);
    }
  }

  // 9.2. 토픽 메시지 조회
  public String getTopicMessages(String topicId, int limit)
      throws PrecheckStatusException, TimeoutException {
    if (!hasOperator()) {
      throw new IllegalStateException("운영자 계정이 설정되지 않았습니다");
    }

    try (Client client = createClient()) {
      TopicId topic = TopicId.fromString(topicId);

      // 메시지를 저장할 리스트
      List<String> messages = new ArrayList<>();
      CountDownLatch latch = new CountDownLatch(1);

      // TopicMessageQuery 생성 - 처음부터 조회
      TopicMessageQuery query = new TopicMessageQuery()
          .setTopicId(topic)
          .setStartTime(Instant.EPOCH) // 처음부터
          .setLimit(limit);

      // 메시지 수신 핸들러 설정
      query.subscribe(client, (message) -> {
        String messageContent = new String(message.contents, StandardCharsets.UTF_8);
        String messageInfo = String.format(
            "메시지 #%d (시간: %s): %s",
            message.sequenceNumber,
            message.consensusTimestamp,
            messageContent);
        messages.add(messageInfo);

        // limit에 도달하면 latch 해제
        if (messages.size() >= limit) {
          latch.countDown();
        }
      });

      // 5초 대기 후 구독 종료 (시간을 늘림)
      try {
        latch.await(5, TimeUnit.SECONDS);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }

      if (messages.isEmpty()) {
        return String.format("토픽 %s에서 메시지를 찾을 수 없습니다. (토픽이 존재하지 않거나 메시지가 없을 수 있습니다)", topicId);
      }

      StringBuilder result = new StringBuilder();
      result.append(String.format("토픽 %s 메시지 (%d개):\n\n", topicId, messages.size()));
      for (String message : messages) {
        result.append(message).append("\n");
      }

      return result.toString();
    }
  }

  // 9.3. 트랜잭션 조회
  public String getTransactionInfo(String transactionId)
      throws PrecheckStatusException, TimeoutException {
    if (!hasOperator()) {
      throw new IllegalStateException("운영자 계정이 설정되지 않았습니다");
    }

    try (Client client = createClient()) {
      TransactionId txId = TransactionId.fromString(transactionId);

      // TransactionRecordQuery 사용
      TransactionRecordQuery query = new TransactionRecordQuery()
          .setTransactionId(txId);

      TransactionRecord record = query.execute(client);

      StringBuilder result = new StringBuilder();
      result.append("트랜잭션 정보:\n\n");
      result.append("Transaction ID: ").append(record.transactionId).append("\n");
      result.append("Consensus Timestamp: ").append(record.consensusTimestamp).append("\n");
      result.append("Status: ").append(record.receipt.status).append("\n");
      result.append("Transaction Fee: ").append(record.transactionFee).append(" tinybars\n");

      if (record.receipt.accountId != null) {
        result.append("Created Account ID: ").append(record.receipt.accountId).append("\n");
      }
      if (record.receipt.tokenId != null) {
        result.append("Created Token ID: ").append(record.receipt.tokenId).append("\n");
      }
      if (record.receipt.topicId != null) {
        result.append("Created Topic ID: ").append(record.receipt.topicId).append("\n");
      }
      if (record.receipt.contractId != null) {
        result.append("Created Contract ID: ").append(record.receipt.contractId).append("\n");
      }
      if (record.receipt.fileId != null) {
        result.append("Created File ID: ").append(record.receipt.fileId).append("\n");
      }
      if (record.receipt.topicSequenceNumber != null && record.receipt.topicSequenceNumber > 0) {
        result.append("Topic Sequence Number: ").append(record.receipt.topicSequenceNumber).append("\n");
      }

      if (record.transfers != null && !record.transfers.isEmpty()) {
        result.append("\nTransfers:\n");
        for (Transfer transfer : record.transfers) {
          result.append("  ").append(transfer.accountId).append(": ").append(transfer.amount).append(" tinybars\n");
        }
      }

      return result.toString();
    }
  }

  // 10. 스마트 컨트랙트 배포
  public String deployContract(byte[] bytecode)
      throws PrecheckStatusException, TimeoutException, ReceiptStatusException {
    if (!hasOperator()) {
      throw new IllegalStateException("운영자 계정이 설정되지 않았습니다");
    }
    try (Client client = createClient()) {
      // 먼저 파일로 bytecode 업로드
      TransactionResponse fileResponse = new FileCreateTransaction()
          .setContents(bytecode)
          .setKeys(client.getOperatorPublicKey())
          .execute(client);

      FileId bytecodeFileId = Objects.requireNonNull(fileResponse.getReceipt(client).fileId,
          "Bytecode file ID is required");

      // 컨트랙트 생성
      TransactionResponse contractResponse = new ContractCreateTransaction()
          .setBytecodeFileId(bytecodeFileId)
          .setGas(100000)
          .execute(client);

      ContractId contractId = contractResponse.getReceipt(client).contractId;

      return String.format("스마트 컨트랙트 배포 완료!%nContract ID: %s", contractId);
    }
  }

  private Client createClient() {
    Client client = switch (network.toLowerCase()) {
      case "mainnet" -> Client.forMainnet();
      case "previewnet" -> Client.forPreviewnet();
      default -> Client.forTestnet();
    };

    // 서명자가 필요한 트랜잭션을 위해 운영자 설정
    if (hasOperator()) {
      client.setOperator(AccountId.fromString(operatorIdEnv), PrivateKey.fromString(operatorKeyEnv));
    }

    return client;
  }
}
