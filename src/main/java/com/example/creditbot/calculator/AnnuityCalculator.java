package com.example.creditbot.calculator;

import com.example.creditbot.model.LoanRequest;
import com.example.creditbot.model.PaymentSchedule;
import com.example.creditbot.model.PaymentSchedule.MonthlyPayment;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class AnnuityCalculator implements PaymentCalculator{

    @Override
    public PaymentSchedule calculate(LoanRequest request) {
        BigDecimal amount = request.getAmount();
        int n = request.getTermMonths();
        BigDecimal annualRate = request.getAnnualRate();

        BigDecimal monthlyRate = annualRate.divide(BigDecimal.valueOf(1200), MathContext.DECIMAL128);

        BigDecimal onePlusRatePowN = (BigDecimal.ONE.add(monthlyRate)).pow(n, MathContext.DECIMAL128);
        BigDecimal annuityCoeff = monthlyRate.multiply(onePlusRatePowN)
                .divide(onePlusRatePowN.subtract(BigDecimal.ONE), MathContext.DECIMAL128);

        BigDecimal monthlyPayment = amount.multiply(annuityCoeff).setScale(2, RoundingMode.HALF_UP);

        List<MonthlyPayment> payments = new ArrayList<>();
        BigDecimal remainingDebt = amount;
        BigDecimal totalInterest = BigDecimal.ZERO;

        for (int month = 1; month <= n; month++) {
            BigDecimal interest = remainingDebt.multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP);
            BigDecimal principal = monthlyPayment.subtract(interest);

            if (month == n) {
                principal = remainingDebt;
            }
            BigDecimal total = principal.add(interest);
            remainingDebt = remainingDebt.subtract(principal);

            totalInterest = totalInterest.add(interest);
            payments.add(new MonthlyPayment(month, principal, interest, total));
        }

        PaymentSchedule schedule = new PaymentSchedule();
        schedule.setPayments(payments);
        schedule.setTotalInterest(totalInterest);
        schedule.setTotalPayment(amount.add(totalInterest));
        return schedule;
    }
}
