package com.example.creditbot.bot.handler;

import com.example.creditbot.bot.CommandHandler;
import com.example.creditbot.bot.CreditBot;
import com.example.creditbot.bot.ReplyKeyboardFactory;
import org.telegram.telegrambots.meta.api.objects.Update;

public class StartHandler implements CommandHandler {
    @Override
    public void handle(CreditBot bot, Update update) {
        Long chatId = update.getMessage().getChatId();
        String text = """
                Привет! Я бот для расчёта кредитов.

                Команды:
                /calculate сумма срок ставка тип
                Пример: /calculate 100000 12 15 annuity
                (тип: annuity или diff)

                /history - история ваших запросов
                /admin пароль - аналитика для менеджеров
                """;
        bot.sendText(chatId, text, false, ReplyKeyboardFactory.createMainKeyboard());
    }
}
