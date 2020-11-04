package com.banking.server.configuration;

import com.banking.server.domain.Account;
import com.banking.server.repository.AccountDataStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class BeanConfig {

  @Bean
  public AccountDataStore getAccountDataStore() {

    Account account1 = new Account(newId(), 1111, 1212, 20000.0);
    Account account2 = new Account(newId(), 2222, 2323, 10000.0);
    Account account3 = new Account(newId(), 3333, 3434, 5000.0);

    AccountDataStore dataStore = new AccountDataStore();
    dataStore.addAccount(account1);
    dataStore.addAccount(account2);
    dataStore.addAccount(account3);

    return dataStore;
  }

  private UUID newId() {
    return UUID.randomUUID();
  }
}
