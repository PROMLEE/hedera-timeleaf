package com.example.hedera.service;

import com.hedera.hashgraph.sdk.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.concurrent.TimeoutException;

@Service
public class HederaService {

  @Value("${app.hedera.network:testnet}")
  private String network;

  @Value("${HEDERA_ACCOUNT_ID:}")
  private String operatorIdEnv;

  @Value("${HEDERA_PRIVATE_KEY:}")
  private String operatorKeyEnv;

  public String getNetwork() {
    return network;
  }

  public boolean hasOperator() {
    return operatorIdEnv != null && !operatorIdEnv.isBlank()
        && operatorKeyEnv != null && !operatorKeyEnv.isBlank();
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
      AccountId sender = client.getOperatorAccountId();
      AccountId receiver = AccountId.fromString(toAccountId);

      TransactionResponse response = new TransferTransaction()
          .addHbarTransfer(sender, Hbar.fromTinybars(-amount))
          .addHbarTransfer(receiver, Hbar.fromTinybars(amount))
          .execute(client);

      TransactionReceipt receipt = response.getReceipt(client);
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
      TransactionResponse response = new TokenCreateTransaction()
          .setTokenName(name)
          .setTokenSymbol(symbol)
          .setDecimals(decimals)
          .setInitialSupply(initialSupply)
          .setTreasuryAccountId(client.getOperatorAccountId())
          .setAdminKey(client.getOperatorPublicKey())
          .setSupplyKey(client.getOperatorPublicKey())
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
      TransactionResponse response = new TokenCreateTransaction()
          .setTokenName(name)
          .setTokenSymbol(symbol)
          .setTokenType(TokenType.NON_FUNGIBLE_UNIQUE)
          .setDecimals(0)
          .setInitialSupply(0)
          .setTreasuryAccountId(client.getOperatorAccountId())
          .setSupplyType(TokenSupplyType.FINITE)
          .setMaxSupply(100)
          .setSupplyKey(client.getOperatorPublicKey())
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
  public String createTopic(String memo)
      throws PrecheckStatusException, TimeoutException, ReceiptStatusException {
    if (!hasOperator()) {
      throw new IllegalStateException("운영자 계정이 설정되지 않았습니다");
    }
    try (Client client = createClient()) {
      TransactionResponse response = new TopicCreateTransaction()
          .setTopicMemo(memo)
          .execute(client);

      TransactionReceipt receipt = response.getReceipt(client);
      TopicId topicId = receipt.topicId;

      return String.format("토픽 생성 완료!%nTopic ID: %s%nMemo: %s",
          topicId, memo);
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

      FileId bytecodeFileId = fileResponse.getReceipt(client).fileId;

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
