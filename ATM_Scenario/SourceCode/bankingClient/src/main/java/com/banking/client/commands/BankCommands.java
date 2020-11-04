package com.banking.client.commands;

import com.banking.client.dtos.AccountDto;
import com.banking.client.dtos.CashDto;
import com.banking.client.exceptions.InsufficientATMBalanceException;
import com.banking.client.exceptions.InsufficientAccountBalanceException;
import com.banking.client.exceptions.MaxNotesExceededException;
import com.banking.client.service.ATMAdministrationService;
import com.banking.client.service.BankingService;
import com.banking.client.util.InputReader;
import com.banking.client.util.ShellHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.HashMap;
import java.util.Map;

/****
 * This class controls all the interactions with the shell
 * through commands - each user mode is treated as a separate shell command,
 * has a dependency on two service classes
 * 1. AdministrationService - to perform admin-mode activities on ATM
 * 2. BankingService - to perform banking for user
 */
@ShellComponent
public class BankCommands {

  @Autowired ShellHelper shellHelper;
  @Autowired InputReader inputReader;
  @Autowired BankingService bankingService;
  @Autowired ATMAdministrationService administrationService;

  /***
   * This method handles all the admin mode operations for the ATM
   * Currently admin can load money of various denominations into the machine
   * Any denomination is supported by the ATM
   */
  @ShellMethod("Operations in admin mode")
  public void adminMode() {

    int totalAmount = 0;
    boolean inputAccepted = false;
    Map<Integer, Integer> denominationMap;

    do {
      try {
        totalAmount = getAmountToLoad();
        denominationMap = getDenominationFor(totalAmount);
        if (isDenominationValidForAmount(totalAmount, denominationMap)) {
          inputAccepted = true;
        } else shellHelper.printError("\nInvalid attempt to add amount, Try again!\n");

      } catch (Exception ex) {
        displayInfo("\nInvalid input passed, please retry!\n");
        denominationMap = new HashMap<>();
      }

    } while (!inputAccepted);

    addAmountToMachine(totalAmount, denominationMap);
  }

  private void addAmountToMachine(int totalAmount, Map<Integer, Integer> denominationMap) {

    CashDto balanceDto = administrationService.addAmount(denominationMap);
    shellHelper.printSuccess("\nAmount successfully added to the machine - " + totalAmount);
    displayDenom("\nBalance in machine after current operation ", balanceDto);
  }

  private int getAmountToLoad() {
    displayInfo("\n==========================");
    displayInfo("--------Admin Operations--------\n");
    displayInfo("Add cash of multiple denominations\n");
    String amount = inputReader.prompt("Enter Amount ");
    insertBlank();
    return Integer.valueOf(amount);
  }

  private Map<Integer, Integer> getDenominationFor(int totalAmount) {

    displayInfo("\nPlease enter count of denominations for the amount " + totalAmount);

    Map<Integer, Integer> denominationMap = new HashMap<>();
    String denominations = inputReader.prompt("Count of denominations ");
    int count = Integer.parseInt(denominations);
    insertBlank();
    getDenominationDetails(denominationMap, count);
    return denominationMap;
  }

  private void getDenominationDetails(Map<Integer, Integer> denominationMap, int count) {
    for (int i = 0; i < count; i++) {
      String denomination = inputReader.prompt("Enter denomination ");
      String countOfNotes = inputReader.prompt("Enter count of notes for " + denomination);
      denominationMap.put(Integer.parseInt(denomination), Integer.parseInt(countOfNotes));
      insertBlank();
    }
  }

  private void displayInfo(String message) {
    shellHelper.printInfo(message);
  }

  /***
   * This method handles all the user-mode commands
   * Currently user can perform following operations
   * 1.LogIn using CardNumber and cardPIN
   * 2.CheckBalance
   * 3.Withdraw amount from ATM
   * @return
   */
  @ShellMethod("Operations in user mode")
  public String userMode() {
    AccountDto accountDto = null;
    do {

      accountDto = loginUser();
      if (accountDto != null) {
        shellHelper.printSuccess(
            "\nSuccess !! Logged-in to account Id -" + accountDto.getAccountId());
        String operation = getUserModeOperationChoice();
        accountDto = executeOperation(accountDto, operation);
      } else shellHelper.printError("\nInvalid login, Please retry ");
    } while (accountDto == null);
    return "";
  }

  private void insertBlank() {
    displayInfo(" ");
  }

  private void displayDenom(String message, CashDto balanceDto) {
    displayInfo(message + balanceDto.getTotalAmount());
    for (Map.Entry<Integer, Integer> entry : balanceDto.getDenominationDetails().entrySet()) {
      displayDenomCount(entry.getKey(), entry.getValue());
    }
  }

  private void displayDenomCount(int denom, int count) {
    displayInfo("Denomination : " + denom + " || Count of notes : " + count);
  }

  private boolean isDenominationValidForAmount(int amount, Map<Integer, Integer> denominationMap) {
    int aggregateAmount = 0;
    for (Map.Entry<Integer, Integer> entry : denominationMap.entrySet()) {
      aggregateAmount += entry.getKey() * entry.getValue();
    }
    return aggregateAmount == amount;
  }

  private AccountDto executeOperation(AccountDto accountDto, String operation) {
    switch (operation) {
      case "1":
        accountDto = performGetBalanceOperation(accountDto);
        break;
      case "2":
        performWithdrawOperation(accountDto);
        break;
      default:
        performExit();
    }
    return accountDto;
  }

  private void performExit() {
    displayInfo("\nLogging out! Bye!!");
  }

  private void performWithdrawOperation(AccountDto accountDto) {
    try {

      displayInfo("\nWithdrawing amount for accountId - " + accountDto.getAccountId());
      String amount = inputReader.prompt("Enter amount to withdraw ");
      String accountId = accountDto.getAccountId().toString();

      CashDto withdrawAmount;
      withdrawAmount = bankingService.withdrawAmount(accountId, Integer.parseInt(amount));
      displayDenom(
          "Successfully withdrew amount " + amount + ", denomination details: ", withdrawAmount);

    } catch (InsufficientATMBalanceException
        | InsufficientAccountBalanceException
        | MaxNotesExceededException bex) {
      shellHelper.printError(bex.getMessage());
    } catch (Exception ex) {
      shellHelper.printError("\nOperation failed, please retry!");
    }
  }

  private AccountDto performGetBalanceOperation(AccountDto accountDto) {
    displayInfo("\nRetrieving balance for accountId - " + accountDto.getAccountId());
    accountDto = bankingService.getBalanceFor(accountDto.getAccountId().toString());
    displayInfo(
        "Balance in accountId :"
            + accountDto.getAccountId()
            + " is "
            + accountDto.getBalanceAmount());
    return accountDto;
  }

  private String getUserModeOperationChoice() {
    displayInfo("\n--------User Operations--------");
    displayInfo("1. CheckBalance");
    displayInfo("2. Withdraw Amount");
    displayInfo("3. Exit\n");
    return inputReader.prompt("Enter serial no of operation to perform ");
  }

  private AccountDto loginUser() {
    AccountDto accountDto = null;
    try {
      displayInfo("\nPlease Login to your account");
      String cardNumber = inputReader.prompt("Enter card number");
      String cardPIN = inputReader.prompt("Enter card PIN ");
      displayInfo("\nAttempting to authenticate user with cardNumber " + cardNumber);

      accountDto = bankingService.getAccountFor(cardNumber, cardPIN);
    } catch (Exception ex) {

    }
    return accountDto;
  }
}
