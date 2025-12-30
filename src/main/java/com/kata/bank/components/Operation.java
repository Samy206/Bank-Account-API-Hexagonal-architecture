package com.kata.bank.components;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull; 

public class Operation {

    @NotNull
    private LocalDate date;
    private float amount;
    private String label;

    public Operation(LocalDate date, float amount, String label) {
        this.date = date;
        this.amount = amount;
        this.label = label;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate localDate) {
        date = localDate;
    }

    public float getAmount() {
        return amount;
    }

    public String getLabel() {
        return label;
    }

}
