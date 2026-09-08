package com.example.creditbot.calculator;

import com.example.creditbot.model.LoanRequest;
import com.example.creditbot.model.PaymentSchedule;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class DifferentiatedCalculator implements PaymentCalculator {

    @Override
    public PaymentSchedule calculate(LoanRequest request) {
        BigDecimal amount = request.getAmount();
        int n = request.getTermMonths();
        BigDecimal annualRate = request.getAnnualRate();

        BigDecimal monthlyRate = annualRate.divide(BigDecimal.valueOf(1200), MathContext.DECIMAL128);

        BigDecimal principalPayment = amount.divide(BigDecimal.valueOf(n), 2, RoundingMode.HALF_UP);

        BigDecimal remainingDebt = amount;
        List<PaymentSchedule.MonthlyPayment> payments = new ArrayList<>();
        BigDecimal totalInterest = BigDecimal.ZERO;

        for (int month = 1; month <= n; month++) {
            BigDecimal interest = remainingDebt.multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP);
            BigDecimal principal = principalPayment;
            if (month == n) {
                principal = remainingDebt; // последний месяц закрываем остаток
            }
            BigDecimal total = principal.add(interest);
            remainingDebt = remainingDebt.subtract(principal);

            totalInterest = totalInterest.add(interest);
            payments.add(new PaymentSchedule.MonthlyPayment(month, principal, interest, total));
        }

        PaymentSchedule schedule = new PaymentSchedule();
        schedule.setPayments(payments);
        schedule.setTotalInterest(totalInterest);
        schedule.setTotalPayment(amount.add(totalInterest));
        return schedule;
    }
}
