package com.banking.client.service;

import com.banking.client.dtos.AccountDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

/****
 * This class abstracts the Bank server REST interactions
 * Currently supports
 * 1.getAccountFor - get accountId for card details
 * 2.getBalanceFor - get balance for accountId
 * 3.updateBalance - update balance amount for accountId
 */
@Component
public class BankingServer {

  private static final String GET_ACCOUNT_URL = "http://localhost:8080/v1/bank/account";
  private static final String GET_BALANCE_URL = "http://localhost:8080/v1/bank/account/{id}";
  private static final String PATCH_BALANCE_URL =
      "http://localhost:8080/v1/bank/account/{id}/balanceAmount";

  @Autowired RestTemplate template;

  /****
   * Get the account for the provided card details
   * @param cardNumber
   * @param cardPIN
   * @return
   */
  public AccountDto getAccountFor(String cardNumber, String cardPIN) {
    HttpHeaders headers = getHttpHeaders();
    String uri =
        fromHttpUrl(GET_ACCOUNT_URL)
            .queryParam("cardNumber", cardNumber)
            .queryParam("cardPIN", cardPIN)
            .toUriString();

    HttpEntity<AccountDto> response =
        template.exchange(uri, HttpMethod.GET, getHttpEntity(headers), AccountDto.class);

    return response.getBody();
  }

  private HttpEntity<?> getHttpEntity(HttpHeaders headers) {
    return new HttpEntity<>(headers);
  }

  /****
   * Get Balance Amount for account Id
   * @param accountId
   * @return
   */
  public AccountDto getBalanceFor(String accountId) {
    HttpHeaders headers = getHttpHeaders();

    String uri =
        fromUriString(GET_BALANCE_URL).buildAndExpand(getIdHeader(accountId)).toUriString();

    HttpEntity<AccountDto> response =
        template.exchange(uri, HttpMethod.GET, getHttpEntity(headers), AccountDto.class);
    return response.getBody();
  }

  private Map<String, String> getIdHeader(String accountId) {
    Map<String, String> params = new HashMap<>();
    params.put("id", accountId);
    return params;
  }

  /*****
   * Update Balance amount for AccountId
   * @param accountId
   * @param amount
   */
  public void updateBalance(String accountId, int amount) {
    HttpHeaders headers = getHttpHeaders();

    String uri =
        fromUriString(PATCH_BALANCE_URL)
            .queryParam("balanceAmount", amount)
            .buildAndExpand(getIdHeader(accountId))
            .toUriString();

    template.exchange(uri, HttpMethod.PATCH, getHttpEntity(headers), Void.class);
  }

  private HttpHeaders getHttpHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Accept", MediaType.APPLICATION_JSON.toString());
    return headers;
  }
}
