package com.banking.server.service;

import com.banking.server.controller.AccountDto;
import com.banking.server.domain.Account;
import com.banking.server.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AccountService {

  private final AccountRepository repository;

  @Autowired
  public AccountService(AccountRepository repository) {
    this.repository = repository;
  }

  /****
   * Get account for the card number and PIN provided
   * throws ResourceNotFoundException if an Account is not found
   * @param cardNumber
   * @param cardPIN
   * @return
   */
  public AccountDto getAccountFor(int cardNumber, int cardPIN) {
    Account account = repository.getAccount(cardNumber, cardPIN);
    return getAccountDtoFor(account.getAccountId());
  }

  /***
   * Get the balance for the accountId
   * throws ResourceNotFoundException if an Account is not found
   * @param accountId
   * @return
   */
  public AccountDto getBalanceFor(UUID accountId) {
    Account account = repository.getAccount(accountId);
    return getAccountDtoFor(account);
  }

  /*****
   * Api to withdraw amount from a particular Account
   * throws ResourceNotFoundException if an Account is not found
   * @param accountId
   * @param amount
   */
  public void updateBalance(UUID accountId, double amount) {
    Account account = repository.getAccount(accountId);
    account.updateBalance(amount);
    repository.save(account);
  }

  private AccountDto getAccountDtoFor(UUID accountId) {
    AccountDto accountDto = new AccountDto();
    accountDto.setAccountId(accountId);
    return accountDto;
  }

  private AccountDto getAccountDtoFor(Account account) {
    AccountDto accountDto = new AccountDto();
    accountDto.setAccountId(account.getAccountId());
    accountDto.setBalanceAmount(account.getBalanceAmount());
    return accountDto;
  }
}
