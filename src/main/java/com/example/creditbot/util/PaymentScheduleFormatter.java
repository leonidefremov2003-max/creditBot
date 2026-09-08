package com.example.creditbot.util;

import com.example.creditbot.model.PaymentSchedule;
import com.example.creditbot.model.PaymentSchedule.MonthlyPayment;

public class PaymentScheduleFormatter {
    public static String toTelegramHtml(PaymentSchedule schedule) {
        StringBuilder sb = new StringBuilder();
        sb.append("<b>График платежей</b>\n");
        sb.append("<pre>");
        sb.append(String.format("%-6s | %10s | %10s | %10s%n", "Месяц", "Осн.долг", "Проценты", "Платёж"));
        sb.append("-------+------------+------------+-----------\n");
        for (MonthlyPayment p : schedule.getPayments()) {
            sb.append(String.format("%-6d | %10.2f | %10.2f | %10.2f%n",
                    p.getMonth(), p.getPrincipal(), p.getInterest(), p.getTotal()));
        }
        sb.append("</pre>");
        sb.append(String.format("<b>Итого переплата:</b> %.2f%n", schedule.getTotalInterest()));
        sb.append(String.format("<b>Всего выплат:</b> %.2f%n", schedule.getTotalPayment()));
        return sb.toString();
    }
}