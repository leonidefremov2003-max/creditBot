package com.example.creditbot.bot;

import com.example.creditbot.bot.handler.*;
import com.example.creditbot.calculator.CalculationSessionManager;
import com.example.creditbot.config.BotConfig;
import com.example.creditbot.storage.InMemoryRequestStorage;
import com.example.creditbot.storage.RequestStorage;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CreditBot extends TelegramLongPollingBot {

    private final Map<String, CommandHandler> handlers = new HashMap<>();
    private final RequestStorage requestStorage = new InMemoryRequestStorage();
    private final CalculationSessionManager sessionManager = new CalculationSessionManager();
    private final Set<Long> pendingPasswordUsers = new HashSet<>();

    public CreditBot() {
        handlers.put("/start", new StartHandler());
        handlers.put("/calculate", new CalculateHandler(requestStorage, sessionManager));
        handlers.put("/history", new HistoryHandler(requestStorage));
        handlers.put("/admin", new AdminHandler(requestStorage));
        handlers.put("/cancel", new CancelHandler(sessionManager));
    }

    @Override
    public String getBotUsername() {
        return BotConfig.BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BotConfig.BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            String command = text.split(" ")[0];

            // Если пользователь ожидает ввод пароля
            if (pendingPasswordUsers.contains(chatId)) {
                CommandHandler adminHandler = handlers.get("/admin");
                if (adminHandler instanceof AdminHandler) {
                    ((AdminHandler) adminHandler).handlePasswordInput(this, update, text);
                    pendingPasswordUsers.remove(chatId);
                }
                return;
            }

            // Если пользователь находится в сессии пошагового ввода расчёта
            if (sessionManager.hasSession(chatId) && !command.equals("/cancel")) {
                CommandHandler calcHandler = handlers.get("/calculate");
                if (calcHandler instanceof CalculateHandler) {
                    ((CalculateHandler) calcHandler).handleStepInput(this, update);
                }
                return;
            }

            CommandHandler handler = handlers.get(command);
            if (handler != null) {
                handler.handle(this, update);
            } else {
                sendText(chatId, "Неизвестная команда. Используйте /start для подсказки.");
            }
        }
    }

    public void addPendingPasswordUser(Long chatId) {
        pendingPasswordUsers.add(chatId);
    }

    public void sendText(Long chatId, String text) {
        sendText(chatId, text, false, null);
    }

    public void sendText(Long chatId, String text, boolean enableHtml) {
        sendText(chatId, text, enableHtml, null);
    }

    public void sendText(Long chatId, String text, boolean enableHtml, ReplyKeyboardMarkup keyboardMarkup) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        if (enableHtml) {
            message.setParseMode("HTML");
        }
        if (keyboardMarkup != null) {
            message.setReplyMarkup(keyboardMarkup);
        }
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}