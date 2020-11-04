package com.banking.client.config;

import com.banking.client.domain.model.ATMMachine;
import com.banking.client.domain.CashWithdrawalStrategy;
import com.banking.client.domain.WithdrawalStrategyFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
  @Value("${atm.cash.withdrawalStrategy}")
  public String strategyName;

  @Value("${atm.cash.maxNotesPerTransaction}")
  public String maxNotesPerTransactionStr;

  @Bean
  public ATMMachine getATMMachine() {
    CashWithdrawalStrategy strategy;
    strategy = WithdrawalStrategyFactory.getWithdrawalStrategyFor(strategyName);
    int maxNotesPerTransaction = Integer.parseInt(maxNotesPerTransactionStr);

    return new ATMMachine(maxNotesPerTransaction, strategy);
  }
}
