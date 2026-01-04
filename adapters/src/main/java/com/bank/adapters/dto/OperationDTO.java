package com.bank.adapters.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OperationDTO {
    private LocalDate date;
    private BigDecimal amount;
    private String label;

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public OperationDTO(LocalDate date, BigDecimal amount, String label) {

        if(date == null) {
            this.date = LocalDate.now();
        } else {
            this.date = date;
        }


        this.amount = amount;
        this.label = label;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public LocalDate getDate() {
        return date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getLabel() {
        return label;
    }
}
