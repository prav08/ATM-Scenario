package com.banking.server.controller;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AccountDto {
    private UUID accountId;
    private double balanceAmount;
}
