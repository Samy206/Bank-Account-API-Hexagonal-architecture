package com.bank.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Operation {

    private LocalDate date;
    private BigDecimal amount;
    private String label;

    // Default constructor required for Jackson deserialization
    public Operation() {
        this.date = LocalDate.now();
    }

    public Operation(LocalDate date, BigDecimal amount, String label) {
        if(date == null) {
            this.date = LocalDate.now();
        } else {
            this.date = date;
        }
        this.amount = amount;
        this.label = label;
    }


    public Operation(BigDecimal amount, String label) {
        this.date = LocalDate.now();
        this.amount = amount;
        this.label = label;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate localDate) {
        date = localDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getLabel() {
        return label;
    }

}
