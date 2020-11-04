package com.banking.client.domain.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Denomination {

  private HashMap<Integer, Integer> denominationDetails;

  public Map<Integer, Integer> getDenominationDetails() {
    return Collections.unmodifiableMap(denominationDetails);
  }

  public Denomination(Map<Integer, Integer> denominationMap) {
    denominationDetails = new HashMap<>();
    denominationMap.forEach((key, value) -> this.denominationDetails.put(key, value));
  }

  public Denomination() {
    denominationDetails = new HashMap<>();
  }

  public void add(int denomination, int countOfNotes) {
    if (denominationDetails.containsKey(denomination)) {
      int aggregatedCount = denominationDetails.get(denomination) + countOfNotes;
      denominationDetails.put(denomination, aggregatedCount);
    } else {
      denominationDetails.put(denomination, countOfNotes);
    }
  }

  public void withdraw(int denomination, int countOfNotes) {
    if (denominationDetails.containsKey(denomination)) {
      int aggregatedCount = denominationDetails.get(denomination) - countOfNotes;
      denominationDetails.put(denomination, aggregatedCount);
    } else {
      denominationDetails.put(denomination, countOfNotes);
    }
  }
}
