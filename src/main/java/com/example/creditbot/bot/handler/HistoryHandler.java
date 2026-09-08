package com.example.creditbot.bot.handler;

import com.example.creditbot.bot.CommandHandler;
import com.example.creditbot.bot.CreditBot;
import com.example.creditbot.model.UserRequest;
import com.example.creditbot.storage.RequestStorage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class HistoryHandler implements CommandHandler {

    private final RequestStorage storage;

    public HistoryHandler(RequestStorage storage) {
        this.storage = storage;
    }

    @Override
    public void handle(CreditBot bot, Update update) {
        Long chatId = update.getMessage().getChatId();
        List<UserRequest> userRequests = storage.getByUserId(chatId);

        if (userRequests.isEmpty()) {
            bot.sendText(chatId, "У вас пока нет запросов.");
            return;
        }

        StringBuilder sb = new StringBuilder("Ваши запросы:\n");
        for (UserRequest req : userRequests) {
            sb.append(req).append("\n");
        }
        bot.sendText(chatId, sb.toString());
    }
}