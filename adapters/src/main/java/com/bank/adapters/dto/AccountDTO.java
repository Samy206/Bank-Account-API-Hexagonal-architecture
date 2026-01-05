package com.bank.adapters.dto;

import java.math.BigDecimal;
import java.util.List;
import com.bank.domain.enums.AccountType;

public class AccountDTO {
    protected String accountNumber;
    protected BigDecimal balance = BigDecimal.ZERO;
    protected AccountType accountType;
    protected List<OperationDTO> operations;
    protected BigDecimal authorizedOverdraw;
    protected BigDecimal overdraw;
}
