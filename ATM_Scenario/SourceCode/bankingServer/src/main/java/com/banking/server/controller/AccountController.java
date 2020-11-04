package com.banking.server.controller;

import com.banking.server.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
public class AccountController {

  private final AccountService service;

  @Autowired
  public AccountController(AccountService service) {
    this.service = service;
  }

  @GetMapping(value = "/v1/bank/account", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<AccountDto> getAccountFor(
      @RequestParam int cardNumber, @RequestParam int cardPIN) {
    AccountDto account = service.getAccountFor(cardNumber, cardPIN);
    return ResponseEntity.ok().body(account);
  }

  @GetMapping(value = "/v1/bank/account/{id}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<AccountDto> getBalanceFor(@PathVariable UUID id) {
    AccountDto account = service.getBalanceFor(id);
    return ResponseEntity.ok().body(account);
  }

  @PatchMapping(value = "/v1/bank/account/{id}/balanceAmount")
  public void updateBalanceAmount(@PathVariable UUID id, @RequestParam double balanceAmount) {
    service.updateBalance(id, balanceAmount);
  }
}
