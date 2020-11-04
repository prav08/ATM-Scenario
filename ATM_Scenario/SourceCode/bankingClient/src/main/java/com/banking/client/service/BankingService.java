package com.banking.client.service;

import com.banking.client.domain.model.ATMMachine;
import com.banking.client.domain.model.Denomination;
import com.banking.client.dtos.AccountDto;
import com.banking.client.dtos.CashDto;
import com.banking.client.exceptions.InsufficientATMBalanceException;
import com.banking.client.exceptions.InsufficientAccountBalanceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*****
 * Contains the apis for interaction with the bank server
 * Supports following operations
 * 1. getAccountFor - get account details for provided card details
 * 2. getBalancefor - get balance amount for provided accountId
 * 3. withdrawAmount - withdraw amount from atm
 */
@Service
public class BankingService {

  @Autowired ATMMachine machine;
  @Autowired
  BankingServer server;

  /***
   * Get the account details for the provided card details
   * @param cardNumber
   * @param cardPIN
   * @return
   */
  public AccountDto getAccountFor(String cardNumber, String cardPIN) {
    return server.getAccountFor(cardNumber, cardPIN);
  }

  /***
   * Get balance amount for the accountId
   * @param accountId
   * @return
   */
  public AccountDto getBalanceFor(String accountId) {
    AccountDto accountDto = server.getBalanceFor(accountId);
    return accountDto;
  }

  /****
   * Withdraw amount from atm
   * 1.validates if the account has sufficient funds by checking balance
   * 2.validates if the atm machine has sufficient funds by checking available amount
   * 3.updates the account through update api on the server to reflect withdrawal
   * Throws insufficient exceptions if account or atm has insufficient funds
   * @param accountId
   * @param amountToWithdraw
   * @return
   */
  public CashDto withdrawAmount(String accountId, int amountToWithdraw) {

    int currentBalance = (int) server.getBalanceFor(accountId).getBalanceAmount();

    if (isValidBalance(amountToWithdraw, currentBalance)) {
      atmHasSufficientBalance(amountToWithdraw);
      return withdrawAmount(accountId, amountToWithdraw, currentBalance);
    }
    throw new InsufficientAccountBalanceException(getExDetail(accountId, amountToWithdraw));
  }

  private String getExDetail(String accountId, int amount) {
    return "Account " + accountId + " contains insufficient balance cannot withdraw " + amount;
  }

  private CashDto withdrawAmount(String accountId, int amount, int balance) {

    Denomination denom = machine.withdrawAmount(amount);
    server.updateBalance(accountId, balance - amount);
    return new CashDto(amount, denom.getDenominationDetails());
  }

  private boolean isValidBalance(int amountToWithdraw, int currentBalance) {
    return currentBalance >= amountToWithdraw;
  }

  private boolean atmHasSufficientBalance(int amountToWithdraw) {
    if (isValidBalance(amountToWithdraw, machine.getAvailableBalance().getTotalAmount())) {
      return true;
    }
    throw new InsufficientATMBalanceException(
        "ATM contains insufficient balance cannot withdraw " + amountToWithdraw);
  }
}
