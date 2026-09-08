package com.example.creditbot.bot.handler;

import com.example.creditbot.bot.CommandHandler;
import com.example.creditbot.bot.CreditBot;
import com.example.creditbot.calculator.CalculationSessionManager;
import org.telegram.telegrambots.meta.api.objects.Update;

public class CancelHandler implements CommandHandler {

    private final CalculationSessionManager sessionManager;

    public CancelHandler(CalculationSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void handle(CreditBot bot, Update update) {
        Long chatId = update.getMessage().getChatId();
        sessionManager.cancelSession(chatId);
        bot.sendText(chatId, "Режим ввода отменён.");
    }
}
