package com.example.creditbot.analytics;

import com.example.creditbot.model.LoanRequest;
import com.example.creditbot.model.UserRequest;
import com.example.creditbot.storage.RequestStorage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnalyticsService {

    private final RequestStorage storage;

    public AnalyticsService(RequestStorage storage) {
        this.storage = storage;
    }

    public String getFullAnalytics() {
        StringBuilder sb = new StringBuilder();
        sb.append("Всего запросов: ").append(storage.getAll().size()).append("\n");
        sb.append("Средняя сумма: ").append(getAverageAmount()).append("\n");
        sb.append("По типам платежей: ").append(getPopularPaymentTypes()).append("\n");
        sb.append("По месяцам: ").append(getRequestsByMonth());
        return sb.toString();
    }

    public String getFilteredByAmount(BigDecimal min, BigDecimal max) {
        List<UserRequest> filtered = storage.filterByAmount(min, max);
        return formatRequestList(filtered);
    }

    public String getFilteredByPaymentType(LoanRequest.PaymentType type) {
        List<UserRequest> filtered = storage.filterByPaymentType(type);
        return formatRequestList(filtered);
    }

    private BigDecimal getAverageAmount() {
        List<UserRequest> all = storage.getAll();
        if (all.isEmpty()) return BigDecimal.ZERO;
        BigDecimal sum = all.stream()
                .map(req -> req.getLoanRequest().getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return sum.divide(BigDecimal.valueOf(all.size()), 2, RoundingMode.HALF_UP);
    }

    private String getPopularPaymentTypes() {
        Map<LoanRequest.PaymentType, Long> counts = storage.getAll().stream()
                .collect(Collectors.groupingBy(
                        req -> req.getLoanRequest().getPaymentType(),
                        Collectors.counting()
                ));
        return counts.toString();
    }

    private String getRequestsByMonth() {
        Map<String, Long> monthCounts = storage.getAll().stream()
                .collect(Collectors.groupingBy(
                        req -> req.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM")),
                        Collectors.counting()
                ));
        return monthCounts.toString();
    }

    private String formatRequestList(List<UserRequest> requests) {
        if (requests.isEmpty()) return "Нет записей, удовлетворяющих условиям.";
        StringBuilder sb = new StringBuilder("Найдено записей: ").append(requests.size()).append("\n");
        for (UserRequest req : requests) {
            sb.append(req).append("\n");
        }
        return sb.toString();
    }
}