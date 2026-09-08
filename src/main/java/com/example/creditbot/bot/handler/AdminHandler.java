package com.example.creditbot.bot.handler;

import com.example.creditbot.analytics.AnalyticsService;
import com.example.creditbot.bot.CommandHandler;
import com.example.creditbot.bot.CreditBot;
import com.example.creditbot.config.BotConfig;
import com.example.creditbot.storage.RequestStorage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class AdminHandler implements CommandHandler {

    private final RequestStorage storage;

    public AdminHandler(RequestStorage storage) {
        this.storage = storage;
    }

    @Override
    public void handle(CreditBot bot, Update update) {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        String[] parts = text.split(" ");


        if (parts.length < 2) {
            bot.sendText(chatId, "Введите пароль:");
            bot.addPendingPasswordUser(chatId);
            return;
        }


        String password = parts[1];
        checkPasswordAndShowAnalytics(bot, chatId, password);
    }


    public void handlePasswordInput(CreditBot bot, Update update, String password) {
        Long chatId = update.getMessage().getChatId();
        checkPasswordAndShowAnalytics(bot, chatId, password.trim());
    }

    private void checkPasswordAndShowAnalytics(CreditBot bot, Long chatId, String password) {
        if (!BotConfig.ADMIN_PASSWORD.equals(password)) {
            bot.sendText(chatId, "Неверный пароль.");
            return;
        }


        AnalyticsService analyticsService = new AnalyticsService(storage);
        bot.sendText(chatId, "Аналитика по запросам:\n" + analyticsService.getFullAnalytics());
    }
}