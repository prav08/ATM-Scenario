package com.banking.client.service;

import com.banking.client.domain.model.ATMMachine;
import com.banking.client.domain.model.Denomination;
import com.banking.client.dtos.CashDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ATMAdministrationService {

  @Autowired ATMMachine machine;

  public CashDto addAmount(Map<Integer, Integer> denominationmap) {
    Denomination denomination = new Denomination(denominationmap);
    machine.addAmount(denomination);
    return getCurrentMachineBalance();
  }

  public CashDto getCurrentMachineBalance() {
    return machine.getAvailableBalance();
  }
}
