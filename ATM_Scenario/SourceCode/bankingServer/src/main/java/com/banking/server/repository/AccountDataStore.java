package com.banking.server.repository;

import com.banking.server.domain.Account;
import com.banking.server.exceptions.ResourceNotFoundException;

import java.util.HashMap;
import java.util.UUID;

/****
 * Represents a datastore to persist the account details in the bank
 * this acts as a mock datasource that gets initialized during application
 * startup startup with preloaded data.
 */
public class AccountDataStore {
  HashMap<String, Account> accountCardMap;
  HashMap<UUID, Account> accountIdMap;

  /***
   * GetAccount details for the specified input data
   * throws ResourceNotFoundException if no account found
   * @param cardNumber
   * @param cardPIN
   * @return
   */
  public Account getAccountFor(int cardNumber, int cardPIN) {
    String accountKey = getKeyFor(cardNumber, cardPIN);
    if (!accountCardMap.containsKey(accountKey))
      throw new ResourceNotFoundException(
          "Account not found for cardNumber - " + cardNumber + " and PIN " + cardPIN);

    return this.accountCardMap.get(accountKey);
  }

  /****
   * Get Account details for the specified accountId
   * throws ResourceNotFoundException if not account found
   * @param accountId
   * @return
   */
  public Account getAccountFor(UUID accountId) {
    if (!accountIdMap.containsKey(accountId))
      throw new ResourceNotFoundException("Account not found for id " + accountId.toString());

    return this.accountIdMap.get(accountId);
  }

  /***
   * Update the data store with the new account object
   * to be able to show the change in value of amount withdrawn
   * throug the api
   * Ideally these operations will be performed by a database
   * @param account
   */
  public void update(Account account) {
    this.accountIdMap.put(account.getAccountId(), account);
  }

  /***
   * Add account to the data store in appropriate datastructures
   * currently used while initialization of data store Bean
   * at application startup
   * @param account
   */
  public void addAccount(Account account) {
    String accountKey = getKeyFor(account.getCardNumber(), account.getCardPIN());
    this.accountCardMap.put(accountKey, account);
    this.accountIdMap.put(account.getAccountId(), account);
  }

  private String getKeyFor(int cardNumber, int cardPIN) {
    return String.valueOf(cardNumber) + "-" + String.valueOf(cardPIN);
  }

  public AccountDataStore() {
    accountCardMap = new HashMap<>();
    accountIdMap = new HashMap<>();
  }
}
