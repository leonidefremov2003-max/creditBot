package com.example.creditbot.calculator;

import com.example.creditbot.model.LoanRequest;

public class CalculatorFactory {
    public static PaymentCalculator createCalculator(LoanRequest.PaymentType type) {
        return switch (type) {
            case ANNUITY -> new AnnuityCalculator();
            case DIFFERENTIATED -> new DifferentiatedCalculator();
            default -> throw new IllegalArgumentException("Неизвестный тип платежа");
        };
    }
}
