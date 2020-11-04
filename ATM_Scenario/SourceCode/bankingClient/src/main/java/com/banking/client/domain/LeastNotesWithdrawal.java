package com.banking.client.domain;

import com.banking.client.domain.model.Denomination;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * This class is an implementation of CashWithdrawalStrategy
 * Implements a greedy approach with a focus for dispensing least
 * possible notes from the machine for the requested amount
 */
public class LeastNotesWithdrawal implements CashWithdrawalStrategy {
  @Override
  public Denomination getWithdrawalDenominations(int amount, Denomination availableDenominations) {

    Map<Integer, Integer> withdrawalDetails = new HashMap<>();
    Map<Integer, Integer> denominations = availableDenominations.getDenominationDetails();
    List<Integer> sortedDenoms = getSortedDenominations(denominations);

    int aggregate = 0;
    for (int index = sortedDenoms.size() - 1; index >= 0; index--) {
      if (sortedDenoms.get(index) > (amount - aggregate)) {
        continue;
      }
      int count = (int) Math.floor((amount - aggregate) / sortedDenoms.get(index));
      if (count > denominations.get(sortedDenoms.get(index))) {
        count = denominations.get(sortedDenoms.get(index));
      }
      if (count > 0) {
        aggregate += count * sortedDenoms.get(index);
        withdrawalDetails.put(sortedDenoms.get(index), count);
      }
    }
    withdrawalDetails = getResultMap(amount, withdrawalDetails);
    return new Denomination(withdrawalDetails);
  }

  private Map<Integer, Integer> getResultMap(int withdrawAmount, Map<Integer, Integer> denoms) {
    int totalAmount = 0;
    for (Map.Entry<Integer, Integer> entry : denoms.entrySet()) {
      totalAmount += entry.getKey() * entry.getValue();
    }
    if (totalAmount == withdrawAmount) return denoms;
    return new HashMap<>();
  }

  private List<Integer> getSortedDenominations(Map<Integer, Integer> denominationMap) {

    List<Integer> sortedDenominations = new ArrayList<>(denominationMap.keySet());
    Collections.sort(sortedDenominations);
    return sortedDenominations;
  }
}
