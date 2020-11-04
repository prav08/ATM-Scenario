package com.banking.client.domain;

import com.banking.client.domain.model.Denomination;

/***
 * This class is an implementation of CashWithdrawalStrategy
 * NOT IMPLEMENTED
 */
public class MaxNotesWithdrawal implements CashWithdrawalStrategy {
  @Override
  public Denomination getWithdrawalDenominations(
      int amountToWithdraw, Denomination availableDenominations) {
    return null;
  }
}
