package com.example.creditbot.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PaymentSchedule {
    private List<MonthlyPayment> payments = new ArrayList<>();
    private BigDecimal totalPayment = BigDecimal.ZERO;
    private BigDecimal totalInterest = BigDecimal.ZERO;

    public static class MonthlyPayment {
        private int month;
        private BigDecimal principal;
        private BigDecimal interest;
        private BigDecimal total;

        public MonthlyPayment(int month, BigDecimal principal, BigDecimal interest, BigDecimal total) {
            this.month = month;
            this.principal = principal;
            this.interest = interest;
            this.total = total;
        }

        public int getMonth() { return month; }
        public BigDecimal getPrincipal() { return principal; }
        public BigDecimal getInterest() { return interest; }
        public BigDecimal getTotal() { return total; }

        @Override
        public String toString() {
            return String.format("Месяц %d: осн.долг=%s, проценты=%s, платёж=%s",
                    month, principal, interest, total);
        }
    }

    public List<MonthlyPayment> getPayments() { return payments; }
    public void setPayments(List<MonthlyPayment> payments) { this.payments = payments; }
    public BigDecimal getTotalInterest() { return totalInterest; }
    public void setTotalInterest(BigDecimal totalInterest) { this.totalInterest = totalInterest; }
    public BigDecimal getTotalPayment() { return totalPayment; }
    public void setTotalPayment(BigDecimal totalPayment) { this.totalPayment = totalPayment; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (MonthlyPayment p : payments) {
            sb.append(p).append("\n");
        }
        sb.append("Итого переплата: ").append(totalInterest).append("\n");
        sb.append("Всего выплат: ").append(totalPayment);
        return sb.toString();
    }
}