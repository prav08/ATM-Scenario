package com.banking.client.domain;

import com.banking.client.domain.model.Denomination;

public interface CashWithdrawalStrategy {

  Denomination getWithdrawalDenominations(
      int amountToWithdraw, Denomination availableDenominations);
}
