package com.banking.client.domain.model;

import com.banking.client.domain.CashWithdrawalStrategy;
import com.banking.client.dtos.CashDto;

import java.util.Map;

public class ATMMachine {

  private Cash cashAvailableCurrently;

  public ATMMachine(int maxNotesPerTransaction, CashWithdrawalStrategy withdrawalStrategy) {
    this.cashAvailableCurrently = new Cash(maxNotesPerTransaction, withdrawalStrategy);
  }

  public void addAmount(Denomination denomination) {
    this.cashAvailableCurrently.addAmount(denomination);
  }

  public CashDto getAvailableBalance() {

    int totalAmount = cashAvailableCurrently.getTotalAmount();
    Map<Integer, Integer> denominationMap = cashAvailableCurrently.getDenomination();
    return new CashDto(totalAmount, denominationMap);
  }

  public Denomination withdrawAmount(int amountToWithdraw) {
    return this.cashAvailableCurrently.withdrawAmountDenom(amountToWithdraw);
  }
}
