package com.example.creditbot.bot;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface CommandHandler {
    void handle(CreditBot bot, Update update);
}
