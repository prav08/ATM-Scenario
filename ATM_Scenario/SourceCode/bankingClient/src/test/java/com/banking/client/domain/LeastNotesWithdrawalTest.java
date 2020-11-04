package com.banking.client.domain;

import com.banking.client.domain.model.Denomination;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

class LeastNotesWithdrawalTest {

  LeastNotesWithdrawal sut;

  @BeforeEach
  void setUp() {
    sut = new LeastNotesWithdrawal();
  }

  @AfterEach
  void tearDown() {}

  @Test
  void test_getWithdrawalDenominations_For_ValidAmount() {

    Denomination denoms = getDenominationsWithOne(100, 10);

    Denomination result = sut.getWithdrawalDenominations(500, denoms);

    Map<Integer, Integer> resultMap = result.getDenominationDetails();
    assertEquals(1, resultMap.size());
    assertEquals(5, resultMap.get(100).intValue());
  }

  @Test
  void test_getWithdrawalDenominations_For_InValidAmount() {

    Denomination denoms = getDenominationsWithOne(2000, 10);

    Denomination result = sut.getWithdrawalDenominations(500, denoms);

    Map<Integer, Integer> resultMap = result.getDenominationDetails();
    assertEquals(0, resultMap.size());
  }

  @Test
  void test_getWithdrawalDenominations_For_MatchingValues() {

    Denomination denoms = getDenominationsWithOne(2000, 10);

    Denomination result = sut.getWithdrawalDenominations(20000, denoms);

    Map<Integer, Integer> resultMap = result.getDenominationDetails();
    assertEquals(1, resultMap.size());
    assertEquals(10, resultMap.get(2000).intValue());
  }

  @Test
  void test_getWithdrawalDenominations_For_Invalid_AmountExceed() {

    Denomination denoms = getDenominationsWithOne(2000, 10);

    Denomination result = sut.getWithdrawalDenominations(21000, denoms);

    Map<Integer, Integer> resultMap = result.getDenominationDetails();
    assertEquals(0, resultMap.size());
  }

  @Test
  void test_getWithdrawalDenominations_For_ValidAmount_MultipleDenoms_ValidateLeastNotes() {
    Map<Integer, Integer> denominations = new HashMap<>();
    denominations.put(2000, 10);
    denominations.put(100, 20);
    Denomination denoms = new Denomination(denominations);

    Denomination result = sut.getWithdrawalDenominations(4000, denoms);
    Map<Integer, Integer> resultMap = result.getDenominationDetails();
    assertEquals(1, resultMap.size());
    assertEquals(2, resultMap.get(2000).intValue());
  }

  @Test
  void test_getWithdrawalDenominations_For_ValidAmount_MultipleDenoms_ValidateDiffNotes() {
    Map<Integer, Integer> denominations = new HashMap<>();
    denominations.put(2000, 1);
    denominations.put(100, 20);
    Denomination denoms = new Denomination(denominations);

    Denomination result = sut.getWithdrawalDenominations(4000, denoms);
    Map<Integer, Integer> resultMap = result.getDenominationDetails();
    assertEquals(2, resultMap.size());
    assertEquals(1, resultMap.get(2000).intValue());
    assertEquals(20, resultMap.get(100).intValue());
  }

  private Denomination getDenominationsWithOne(int denomination, int count) {
    Map<Integer, Integer> denomMap = getDenominationMap(denomination, count);
    return new Denomination(denomMap);
  }

  private Map<Integer, Integer> getDenominationMap(int denomination, int count) {
    Map<Integer, Integer> denoms = new HashMap<>();
    denoms.put(denomination, count);
    return denoms;
  }
}
