package com.banking.client.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
public class CashDto {
  @Getter @Setter private int totalAmount;
  @Getter @Setter private Map<Integer, Integer> denominationDetails;
}
