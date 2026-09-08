package com.example.creditbot.storage;


import com.example.creditbot.model.LoanRequest;
import com.example.creditbot.model.UserRequest;

import java.math.BigDecimal;
import java.util.List;

public interface RequestStorage {
    void add(UserRequest request);
    List<UserRequest> getAll();
    List<UserRequest> getByUserId(Long userId);
    List<UserRequest> filterByAmount(BigDecimal min, BigDecimal max);
    List<UserRequest> filterByPaymentType(LoanRequest.PaymentType type);
}
