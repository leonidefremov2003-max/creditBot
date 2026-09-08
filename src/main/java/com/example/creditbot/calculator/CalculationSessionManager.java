package com.example.creditbot.calculator;

import com.example.creditbot.model.LoanRequest;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class CalculationSessionManager {

    private static class LoanRequestBuilder {
        BigDecimal amount;
        Integer months;
        BigDecimal rate;
        LoanRequest.PaymentType type;
        int step = 0;
    }

    private final Map<Long, LoanRequestBuilder> sessions = new HashMap<>();

    public void startSession(Long userId) {
        sessions.put(userId, new LoanRequestBuilder());
    }

    public boolean hasSession(Long userId) {
        return sessions.containsKey(userId);
    }

    public StepResult processInput(Long userId, String input) {
        LoanRequestBuilder builder = sessions.get(userId);
        if (builder == null) {
            return StepResult.error("Сессия не найдена. Начните заново с /calculate.");
        }

        try {
            switch (builder.step) {
                case 0 -> {
                    builder.amount = new BigDecimal(input.trim());
                    builder.step++;
                    return StepResult.next("Введите срок кредита в месяцах:");
                }
                case 1 -> {
                    builder.months = Integer.parseInt(input.trim());
                    builder.step++;
                    return StepResult.next("Введите процентную ставку (годовых):");
                }
                case 2 -> {
                    builder.rate = new BigDecimal(input.trim());
                    builder.step++;
                    return StepResult.next("Введите тип платежа (annuity или diff):");
                }
                case 3 -> {
                    LoanRequest.PaymentType type = LoanRequest.PaymentType.fromString(input.trim());
                    LoanRequest request = new LoanRequest(builder.amount, builder.months, builder.rate, type);
                    sessions.remove(userId);
                    return StepResult.completed(request);
                }
                default -> {
                    sessions.remove(userId);
                    return StepResult.error("Некорректное состояние. Сессия сброшена.");
                }
            }
        } catch (NumberFormatException e) {
            return StepResult.error("Неверный формат числа. Попробуйте ещё раз.");
        } catch (IllegalArgumentException e) {
            return StepResult.error(e.getMessage());
        }
    }

    public void cancelSession(Long userId) {
        sessions.remove(userId);
    }

    public static class StepResult {
        private final boolean finished;
        private final LoanRequest loanRequest;
        private final String message;

        private StepResult(boolean finished, LoanRequest loanRequest, String message) {
            this.finished = finished;
            this.loanRequest = loanRequest;
            this.message = message;
        }

        public static StepResult next(String message) { return new StepResult(false, null, message); }
        public static StepResult completed(LoanRequest request) { return new StepResult(true, request, null); }
        public static StepResult error(String message) { return new StepResult(false, null, message); }

        public boolean isFinished() { return finished; }
        public LoanRequest getLoanRequest() { return loanRequest; }
        public String getMessage() { return message; }
    }
}