package com.banking.client.domain;

public class WithdrawalStrategyFactory {

  public static CashWithdrawalStrategy getWithdrawalStrategyFor(String strategyName) {
    switch (strategyName) {
      case "LeastNotes":
        return new LeastNotesWithdrawal();
      default:
        return new MaxNotesWithdrawal();
    }
  }
}
