package com.example.creditbot.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BotConfig {
    private static final Properties props = new Properties();

    static {
        try (InputStream input = BotConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                props.load(input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static final String BOT_TOKEN = props.getProperty("bot.token", "");
    public static final String BOT_USERNAME = props.getProperty("bot.username", "creditJava_bot");
    public static final String ADMIN_PASSWORD = props.getProperty("admin.password", "admin123");
}
