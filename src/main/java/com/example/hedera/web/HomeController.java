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

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
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

  record TopicForm(@NotBlank(message = "메모를 입력하세요") String memo) {
  }

  record MessageForm(
      @NotBlank(message = "토픽 ID를 입력하세요") String topicId,
      @NotBlank(message = "메시지를 입력하세요") String message) {
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
    model.addAttribute("topicForm", new TopicForm(""));
    model.addAttribute("messageForm", new MessageForm("", ""));
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
      model.addAttribute("result",
          String.format("%s 의 hbars: %s, tokens: %s", form.accountId(), balance.hbars, balance.tokens));
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
    }
    return "index";
  }

  @PostMapping("/query-info")
  public String queryInfo(@RequestParam String accountId, Model model) {
    addAllFormAttributes(model);
    try {
      AccountInfo info = hederaService.getAccountInfo(accountId);
      model.addAttribute("result",
          String.format("계정 정보:%nAccount ID: %s%nKey: %s%nBalance: %s%nMemo: %s",
              info.accountId, info.key, info.balance, info.accountMemo));
    } catch (Exception e) {
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
      model.addAttribute("result", result);
    } catch (Exception e) {
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
      model.addAttribute("result", result);
    } catch (Exception e) {
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
      model.addAttribute("result", result);
    } catch (Exception e) {
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
      model.addAttribute("result", result);
    } catch (Exception e) {
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
      model.addAttribute("result", result);
    } catch (Exception e) {
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
      String result = hederaService.createTopic(form.memo());
      model.addAttribute("result", result);
    } catch (Exception e) {
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
      model.addAttribute("result", result);
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
    }
    return "index";
  }

  private void addCommonAttributes(Model model) {
    model.addAttribute("network", hederaService.getNetwork());
    model.addAttribute("hasOperator", hederaService.hasOperator());
  }

  private void addAllFormAttributes(Model model) {
    addCommonAttributes(model);
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
      model.addAttribute("topicForm", new TopicForm(""));
    }
    if (!model.containsAttribute("messageForm")) {
      model.addAttribute("messageForm", new MessageForm("", ""));
    }
  }
}
