package com.example.creditbot.model;

import java.math.BigDecimal;

public class LoanRequest {
    public enum PaymentType {
        ANNUITY, DIFFERENTIATED;

        public static PaymentType fromString(String value) {
            if (value == null) throw new IllegalArgumentException("Тип платежа не может быть null");
            if (value.equalsIgnoreCase("annuity")) return ANNUITY;
            if (value.equalsIgnoreCase("diff") || value.equalsIgnoreCase("differentiated")) return DIFFERENTIATED;
            throw new IllegalArgumentException("Тип платежа должен быть annuity или diff");
        }
    }

    private BigDecimal amount;
    private int termMonths;
    private BigDecimal annualRate;
    private PaymentType paymentType;

    public LoanRequest(BigDecimal amount, int termMonths, BigDecimal annualRate, PaymentType paymentType) {
        this.amount = amount;
        this.termMonths = termMonths;
        this.annualRate = annualRate;
        this.paymentType = paymentType;
    }


    public BigDecimal getAmount() { return amount; }
    public int getTermMonths() { return termMonths; }
    public BigDecimal getAnnualRate() { return annualRate; }
    public PaymentType getPaymentType() { return paymentType; }

    @Override
    public String toString() {
        return String.format("Сумма=%s, Срок=%d мес, Ставка=%s%%, Тип=%s",
                amount, termMonths, annualRate, paymentType);
    }
}