package com.example.creditbot.calculator;

import com.example.creditbot.model.LoanRequest;
import com.example.creditbot.model.PaymentSchedule;

public interface PaymentCalculator {
    PaymentSchedule calculate(LoanRequest request);
}
