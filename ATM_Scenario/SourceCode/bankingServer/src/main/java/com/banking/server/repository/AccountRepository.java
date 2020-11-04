package com.banking.server.repository;

import com.banking.server.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/***
 * Repository to interact with the dataStore
 * Currently it invokes get and save apis on mock dataStore
 */
@Component
public class AccountRepository {

  @Autowired AccountDataStore accountDataStore;

  public Account getAccount(int cardNumber, int cardPIN) {
    return this.accountDataStore.getAccountFor(cardNumber, cardPIN);
  }

  public Account getAccount(UUID accountId) {
    return this.accountDataStore.getAccountFor(accountId);
  }

  public void save(Account account) {
    this.accountDataStore.update(account);
  }
}
