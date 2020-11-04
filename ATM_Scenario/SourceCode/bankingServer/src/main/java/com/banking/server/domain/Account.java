package com.banking.server.domain;

import com.banking.server.exceptions.InvalidBalanceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

/*****
 * Represents an account in the Banking application
 * provides api for withdrawal of money from account
 * Other apis such as add money, modify account have
 * not been implemented as it is not part of the scenario
 */
@Getter
@AllArgsConstructor
public class Account {

  private UUID accountId;
  private int cardNumber;
  private int cardPIN;
  private double balanceAmount;

  /****
   * api to withdraw specified amount from Account
   * throws OverDraftException If the amount of money attempted
   * to withdraw is more than available balance
   * @param amount
   */
  public void updateBalance(double amount) {
    if (isBalanceNegative(amount))
      throw new InvalidBalanceException("Failed to withdraw amount from account Id " + accountId);

    this.balanceAmount = amount;
  }

  private boolean isBalanceNegative(double amount) {
    return this.balanceAmount < 0;
  }
}
