package com.example.creditbot.storage;

import com.example.creditbot.model.LoanRequest;
import com.example.creditbot.model.UserRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryRequestStorage implements RequestStorage {

    private final List<UserRequest> requests = new ArrayList<>();

    @Override
    public void add(UserRequest request) {
        requests.add(request);
    }

    @Override
    public List<UserRequest> getAll() {
        return new ArrayList<>(requests);
    }

    @Override
    public List<UserRequest> getByUserId(Long userId) {
        return requests.stream()
                .filter(req -> req.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserRequest> filterByAmount(BigDecimal min, BigDecimal max) {
        return requests.stream()
                .filter(req -> {
                    BigDecimal amount = req.getLoanRequest().getAmount();
                    return amount.compareTo(min) >= 0 && amount.compareTo(max) <= 0;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<UserRequest> filterByPaymentType(LoanRequest.PaymentType type) {
        return requests.stream()
                .filter(req -> req.getLoanRequest().getPaymentType() == type)
                .collect(Collectors.toList());
    }
}
