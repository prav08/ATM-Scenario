package com.banking.client.domain.model;

import com.banking.client.domain.CashWithdrawalStrategy;
import com.banking.client.exceptions.MaxNotesExceededException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Cash {

  private Denomination denomination;
  private CashWithdrawalStrategy withdrawStrategy;
  private int maxNotesAllowed;

  public Map<Integer, Integer> getDenomination() {
    return denomination.getDenominationDetails();
  }

  public int getTotalAmount() {

    int totalAmount = 0;
    Map<Integer, Integer> denomDetails = denomination.getDenominationDetails();

    for (Map.Entry<Integer, Integer> entry : denomDetails.entrySet()) {
      totalAmount += entry.getKey() * entry.getValue();
    }
    return totalAmount;
  }

  public Cash(int maxNotesAllowed, CashWithdrawalStrategy withdrawalStrategy) {

    this.maxNotesAllowed = maxNotesAllowed;
    this.withdrawStrategy = withdrawalStrategy;
    this.denomination = new Denomination();
  }

  private void addMoney(int denomination, int countOfNotes) {
    this.denomination.add(denomination, countOfNotes);
  }

  public void addAmount(Denomination denominationToAdd) {
    Map<Integer, Integer> additionalDenomination = denominationToAdd.getDenominationDetails();
    additionalDenomination.forEach(this::addMoney);
  }

  public Denomination withdrawAmountDenom(int amount) {

    Denomination withdrawalDenomination;
    withdrawalDenomination = withdrawStrategy.getWithdrawalDenominations(amount, denomination);

    withdrawAmountDenom(withdrawalDenomination);
    return withdrawalDenomination;
  }

  private void withdrawAmountDenom(Denomination toWithdraw) {
    validateMaxNotesLimit(toWithdraw);
    Map<Integer, Integer> removeDenom = toWithdraw.getDenominationDetails();
    removeDenom.forEach((key, value) -> this.denomination.withdraw(key, value));
  }

  private void validateMaxNotesLimit(Denomination toWithdraw) {
    List<Integer> values = new ArrayList<>(toWithdraw.getDenominationDetails().values());
    int withdrawalAttemptCount = values.stream().mapToInt(Integer::intValue).sum();
    if (withdrawalAttemptCount > maxNotesAllowed)
      throw new MaxNotesExceededException(
          "ERROR!! Attempted to withdraw more than allowed notes count : 40");
  }
}
