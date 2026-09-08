package com.example.creditbot.model;

import java.time.LocalDateTime;

public class UserRequest {
    private Long userId;
    private LoanRequest loanRequest;
    private LocalDateTime timestamp;

    public UserRequest(Long userId, LoanRequest loanRequest) {
        this.userId = userId;
        this.loanRequest = loanRequest;
        this.timestamp = LocalDateTime.now(); // исправлено
    }

    public Long getUserId() { return userId; }
    public LoanRequest getLoanRequest() { return loanRequest; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return String.format("[%s] %s", timestamp, loanRequest);
    }
}