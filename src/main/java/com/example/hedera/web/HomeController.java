package com.example.hedera.web;

import com.example.hedera.service.HederaService;
import com.hedera.hashgraph.sdk.AccountBalance;
import com.hedera.hashgraph.sdk.AccountInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.Valid;

@Controller
public class HomeController {

  record BalanceForm(@NotBlank(message = "계정 ID를 입력하세요 (예: 0.0.1234)") String accountId) {
  }

  record TransferForm(
      @NotBlank(message = "받는 계정 ID를 입력하세요") String toAccountId,
      @Min(value = 1, message = "전송할 금액을 입력하세요 (tinybars)") long amount) {
  }

  record AccountForm(@Min(value = 0, message = "초기 잔액을 입력하세요 (tinybars)") long initialBalance) {
  }

  record TokenForm(
      @NotBlank(message = "토큰 이름을 입력하세요") String name,
      @NotBlank(message = "토큰 심볼을 입력하세요") String symbol,
      @Min(value = 1, message = "발행량을 입력하세요") long initialSupply,
      @Min(value = 0, message = "소수점 자리수를 입력하세요") int decimals) {
  }

  record NFTForm(
      @NotBlank(message = "NFT 이름을 입력하세요") String name,
      @NotBlank(message = "NFT 심볼을 입력하세요") String symbol) {
  }

  record NFTMintForm(
      @NotBlank(message = "토큰 ID를 입력하세요") String tokenId,
      @NotBlank(message = "메타데이터를 입력하세요") String metadata) {
  }

  record TopicForm(
      @NotBlank(message = "메모를 입력하세요") String memo,
      @Min(value = 1800, message = "자동 갱신 기간은 최소 1800초 (30분) 이상이어야 합니다") @Max(value = 7776000, message = "자동 갱신 기간은 최대 7,776,000초 (90일) 이하로 설정해야 합니다") long autoRenewPeriodSeconds) {
  }

  record TopicInfoForm(String topicId) {
  }

  record TopicMessageForm(String topicId, int limit) {
  }

  record MessageForm(
      @NotBlank(message = "토픽 ID를 입력하세요") String topicId,
      @NotBlank(message = "메시지를 입력하세요") String message) {
  }

  record TransactionInfoForm(String transactionId) {
  }

  private final HederaService hederaService;

  public HomeController(HederaService hederaService) {
    this.hederaService = hederaService;
  }

  @GetMapping("/")
  public String index(Model model) {
    addCommonAttributes(model);
    model.addAttribute("balanceForm", new BalanceForm("0.0.3"));
    model.addAttribute("transferForm", new TransferForm("", 100000000L));
    model.addAttribute("accountForm", new AccountForm(100000000L));
    model.addAttribute("tokenForm", new TokenForm("", "", 1000000L, 2));
    model.addAttribute("nftForm", new NFTForm("", ""));
    model.addAttribute("nftMintForm", new NFTMintForm("", ""));
    model.addAttribute("topicForm", new TopicForm("", 7776000L));
    model.addAttribute("topicInfoForm", new TopicInfoForm(""));
    model.addAttribute("topicMessageForm", new TopicMessageForm("", 10));
    model.addAttribute("messageForm", new MessageForm("", ""));
    model.addAttribute("transactionInfoForm", new TransactionInfoForm(""));

    // Operator 정보 추가
    if (hederaService.hasOperator()) {
      try {
        AccountInfo operatorInfo = hederaService.getOperatorInfo();
        model.addAttribute("operatorInfo", operatorInfo);
        model.addAttribute("operatorAccountId", hederaService.getOperatorAccountId());
      } catch (Exception e) {
        // Operator 정보 조회 실패는 무시
      }
    }

    return "index";
  }

  @PostMapping("/query-balance")
  public String queryBalance(@ModelAttribute("balanceForm") @Valid BalanceForm form,
      BindingResult bindingResult, Model model) {
    addAllFormAttributes(model);
    if (bindingResult.hasErrors()) {
      return "index";
    }
    try {
      AccountBalance balance = hederaService.getAccountBalance(form.accountId());
      model.addAttribute("resultKey", "balance");
      model.addAttribute("result",
          String.format("%s 의 hbars: %s, tokens: %s", form.accountId(), balance.hbars, balance.tokens));
    } catch (Exception e) {
      model.addAttribute("resultKey", "balance");
      model.addAttribute("error", e.getMessage());
    }
    return "index";
  }

  @PostMapping("/query-info")
  public String queryInfo(@RequestParam("accountId") String accountId, Model model) {
    addAllFormAttributes(model);
    try {
      AccountInfo info = hederaService.getAccountInfo(accountId);
      model.addAttribute("resultKey", "info");
      model.addAttribute("result",
          String.format("계정 정보:%nAccount ID: %s%nKey: %s%nBalance: %s%nMemo: %s",
              info.accountId, info.key, info.balance, info.accountMemo));
    } catch (Exception e) {
      model.addAttribute("resultKey", "info");
      model.addAttribute("error", e.getMessage());
    }
    return "index";
  }

  @PostMapping("/transfer")
  public String transfer(@ModelAttribute("transferForm") @Valid TransferForm form,
      BindingResult bindingResult, Model model) {
    addAllFormAttributes(model);
    if (bindingResult.hasErrors()) {
      return "index";
    }
    try {
      String result = hederaService.transferHbar(form.toAccountId(), form.amount());
      model.addAttribute("resultKey", "transfer");
      model.addAttribute("result", result);
    } catch (Exception e) {
      model.addAttribute("resultKey", "transfer");
      model.addAttribute("error", e.getMessage());
    }
    return "index";
  }

  @PostMapping("/create-account")
  public String createAccount(@ModelAttribute("accountForm") @Valid AccountForm form,
      BindingResult bindingResult, Model model) {
    addAllFormAttributes(model);
    if (bindingResult.hasErrors()) {
      return "index";
    }
    try {
      String result = hederaService.createAccount(form.initialBalance());
      model.addAttribute("resultKey", "account");
      model.addAttribute("result", result);
    } catch (Exception e) {
      model.addAttribute("resultKey", "account");
      model.addAttribute("error", e.getMessage());
    }
    return "index";
  }

  @PostMapping("/create-token")
  public String createToken(@ModelAttribute("tokenForm") @Valid TokenForm form,
      BindingResult bindingResult, Model model) {
    addAllFormAttributes(model);
    if (bindingResult.hasErrors()) {
      return "index";
    }
    try {
      String result = hederaService.createToken(form.name(), form.symbol(),
          form.initialSupply(), form.decimals());
      model.addAttribute("resultKey", "token");
      model.addAttribute("result", result);
    } catch (Exception e) {
      model.addAttribute("resultKey", "token");
      model.addAttribute("error", e.getMessage());
    }
    return "index";
  }

  @PostMapping("/create-nft")
  public String createNFT(@ModelAttribute("nftForm") @Valid NFTForm form,
      BindingResult bindingResult, Model model) {
    addAllFormAttributes(model);
    if (bindingResult.hasErrors()) {
      return "index";
    }
    try {
      String result = hederaService.createNFT(form.name(), form.symbol());
      model.addAttribute("resultKey", "nft");
      model.addAttribute("result", result);
    } catch (Exception e) {
      model.addAttribute("resultKey", "nft");
      model.addAttribute("error", e.getMessage());
    }
    return "index";
  }

  @PostMapping("/mint-nft")
  public String mintNFT(@ModelAttribute("nftMintForm") @Valid NFTMintForm form,
      BindingResult bindingResult, Model model) {
    addAllFormAttributes(model);
    if (bindingResult.hasErrors()) {
      return "index";
    }
    try {
      String result = hederaService.mintNFT(form.tokenId(), form.metadata().getBytes());
      model.addAttribute("resultKey", "mint");
      model.addAttribute("result", result);
    } catch (Exception e) {
      model.addAttribute("resultKey", "mint");
      model.addAttribute("error", e.getMessage());
    }
    return "index";
  }

  @PostMapping("/create-topic")
  public String createTopic(@ModelAttribute("topicForm") @Valid TopicForm form,
      BindingResult bindingResult, Model model) {
    addAllFormAttributes(model);
    if (bindingResult.hasErrors()) {
      return "index";
    }
    try {
      String result = hederaService.createTopic(form.memo(), form.autoRenewPeriodSeconds());
      model.addAttribute("resultKey", "topic");
      model.addAttribute("result", result);
    } catch (Exception e) {
      model.addAttribute("resultKey", "topic");
      model.addAttribute("error", e.getMessage());
    }
    return "index";
  }

  @PostMapping("/query-topic")
  public String queryTopic(@RequestParam("topicId") String topicId, Model model) {
    System.out.println("DEBUG: query-topic called with topicId: '" + topicId + "'");
    addAllFormAttributes(model);

    // 빈 값 체크를 직접 처리
    if (topicId == null || topicId.trim().isEmpty()) {
      model.addAttribute("resultKey", "topicInfo");
      model.addAttribute("error", "토픽 ID를 입력하세요");
      return "index";
    }

    try {
      String result = hederaService.getTopicInfo(topicId.trim());
      System.out.println("DEBUG: Topic info result: " + result);
      model.addAttribute("resultKey", "topicInfo");
      model.addAttribute("result", result);
    } catch (Exception e) {
      System.out.println("DEBUG: Exception in topic query: " + e.getMessage());
      e.printStackTrace();
      model.addAttribute("resultKey", "topicInfo");
      model.addAttribute("error", e.getMessage());
    }
    return "index";
  }

  @PostMapping("/submit-message")
  public String submitMessage(@ModelAttribute("messageForm") @Valid MessageForm form,
      BindingResult bindingResult, Model model) {
    addAllFormAttributes(model);
    if (bindingResult.hasErrors()) {
      return "index";
    }
    try {
      String result = hederaService.submitMessage(form.topicId(), form.message());
      model.addAttribute("resultKey", "message");
      model.addAttribute("result", result);
    } catch (Exception e) {
      model.addAttribute("resultKey", "message");
      model.addAttribute("error", e.getMessage());
    }
    return "index";
  }

  @PostMapping("/query-topic-messages")
  public String queryTopicMessages(@RequestParam("topicId") String topicId,
      @RequestParam("limit") int limit,
      Model model) {
    addAllFormAttributes(model);
    System.out.println("DEBUG: query-topic-messages called with topicId: '" + topicId + "', limit: " + limit);

    if (topicId == null || topicId.trim().isEmpty()) {
      model.addAttribute("resultKey", "topicMessages");
      model.addAttribute("error", "토픽 ID를 입력하세요");
      return "index";
    }

    if (limit <= 0) {
      limit = 10; // 기본값
    }

    try {
      String result = hederaService.getTopicMessages(topicId.trim(), limit);
      System.out.println("DEBUG: Topic messages result: " + result);
      model.addAttribute("resultKey", "topicMessages");
      model.addAttribute("result", result);
    } catch (Exception e) {
      System.out.println("DEBUG: Exception in topic messages query: " + e.getMessage());
      e.printStackTrace();
      model.addAttribute("resultKey", "topicMessages");
      model.addAttribute("error", e.getMessage());
    }
    return "index";
  }

  @ModelAttribute("topicInfoForm")
  public TopicInfoForm topicInfoForm() {
    return new TopicInfoForm("");
  }

  @ModelAttribute("topicMessageForm")
  public TopicMessageForm topicMessageForm() {
    return new TopicMessageForm("", 10);
  }

  @PostMapping("/query-transaction")
  public String queryTransaction(@RequestParam("transactionId") String transactionId, Model model) {
    addAllFormAttributes(model);
    System.out.println("DEBUG: query-transaction called with transactionId: '" + transactionId + "'");

    if (transactionId == null || transactionId.trim().isEmpty()) {
      model.addAttribute("resultKey", "transactionInfo");
      model.addAttribute("error", "트랜잭션 ID를 입력하세요");
      return "index";
    }

    try {
      String result = hederaService.getTransactionInfo(transactionId.trim());
      System.out.println("DEBUG: Transaction info result: " + result);
      model.addAttribute("resultKey", "transactionInfo");
      model.addAttribute("result", result);
    } catch (Exception e) {
      System.out.println("DEBUG: Exception in transaction query: " + e.getMessage());
      e.printStackTrace();
      model.addAttribute("resultKey", "transactionInfo");
      model.addAttribute("error", e.getMessage());
    }
    return "index";
  }

  @ModelAttribute("transactionInfoForm")
  public TransactionInfoForm transactionInfoForm() {
    return new TransactionInfoForm("");
  }

  private void addCommonAttributes(Model model) {
    model.addAttribute("network", hederaService.getNetwork());
    model.addAttribute("hasOperator", hederaService.hasOperator());
  }

  private void addAllFormAttributes(Model model) {
    addCommonAttributes(model);

    // Operator 정보 추가
    if (hederaService.hasOperator()) {
      try {
        AccountInfo operatorInfo = hederaService.getOperatorInfo();
        model.addAttribute("operatorInfo", operatorInfo);
        model.addAttribute("operatorAccountId", hederaService.getOperatorAccountId());
      } catch (Exception e) {
        // Operator 정보 조회 실패는 무시
      }
    }

    if (!model.containsAttribute("balanceForm")) {
      model.addAttribute("balanceForm", new BalanceForm(""));
    }
    if (!model.containsAttribute("transferForm")) {
      model.addAttribute("transferForm", new TransferForm("", 100000000L));
    }
    if (!model.containsAttribute("accountForm")) {
      model.addAttribute("accountForm", new AccountForm(100000000L));
    }
    if (!model.containsAttribute("tokenForm")) {
      model.addAttribute("tokenForm", new TokenForm("", "", 1000000L, 2));
    }
    if (!model.containsAttribute("nftForm")) {
      model.addAttribute("nftForm", new NFTForm("", ""));
    }
    if (!model.containsAttribute("nftMintForm")) {
      model.addAttribute("nftMintForm", new NFTMintForm("", ""));
    }
    if (!model.containsAttribute("topicForm")) {
      model.addAttribute("topicForm", new TopicForm("", 7776000L));
    }
    if (!model.containsAttribute("topicInfoForm")) {
      model.addAttribute("topicInfoForm", new TopicInfoForm(""));
    }
    if (!model.containsAttribute("messageForm")) {
      model.addAttribute("messageForm", new MessageForm("", ""));
    }
  }
}
