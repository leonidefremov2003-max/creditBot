package com.example.creditbot.bot.handler;

import com.example.creditbot.bot.CommandHandler;
import com.example.creditbot.bot.CreditBot;
import com.example.creditbot.calculator.CalculationSessionManager;
import com.example.creditbot.calculator.CalculatorFactory;
import com.example.creditbot.calculator.PaymentCalculator;
import com.example.creditbot.model.LoanRequest;
import com.example.creditbot.model.PaymentSchedule;
import com.example.creditbot.model.UserRequest;
import com.example.creditbot.storage.RequestStorage;
import com.example.creditbot.util.PaymentScheduleFormatter;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.math.BigDecimal;

public class CalculateHandler implements CommandHandler {

    private final RequestStorage storage;
    private final CalculationSessionManager sessionManager;

    public CalculateHandler(RequestStorage storage, CalculationSessionManager sessionManager) {
        this.storage = storage;
        this.sessionManager = sessionManager;
    }

    @Override
    public void handle(CreditBot bot, Update update) {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        String[] parts = text.split(" ");

        if (parts.length >= 5) {
            try {
                BigDecimal amount = new BigDecimal(parts[1]);
                int months = Integer.parseInt(parts[2]);
                BigDecimal rate = new BigDecimal(parts[3]);
                LoanRequest.PaymentType type = LoanRequest.PaymentType.fromString(parts[4]);
                LoanRequest request = new LoanRequest(amount, months, rate, type);
                processRequest(bot, chatId, request);
            } catch (Exception e) {
                bot.sendText(chatId, "Ошибка в параметрах: " + e.getMessage() + "\nИспользуйте /cancel для выхода.");
            }
        } else {
            sessionManager.startSession(chatId);
            bot.sendText(chatId, "Введите сумму кредита (например, 100000):");
        }
    }

    public void handleStepInput(CreditBot bot, Update update) {
        Long chatId = update.getMessage().getChatId();
        String input = update.getMessage().getText();

        CalculationSessionManager.StepResult result = sessionManager.processInput(chatId, input);

        if (result.isFinished()) {
            processRequest(bot, chatId, result.getLoanRequest());
        } else {
            bot.sendText(chatId, result.getMessage());
        }
    }

    private void processRequest(CreditBot bot, Long chatId, LoanRequest request) {
        PaymentCalculator calculator = CalculatorFactory.createCalculator(request.getPaymentType());
        PaymentSchedule schedule = calculator.calculate(request);
        storage.add(new UserRequest(chatId, request));
        bot.sendText(chatId, PaymentScheduleFormatter.toTelegramHtml(schedule), true);
    }
}