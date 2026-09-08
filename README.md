# CreditBot — Telegram-бот для расчёта графиков погашения кредитов

Учебный проект на Java, демонстрирующий применение принципов ООП, SOLID, Low Coupling, High Cohesion, а также паттерна Factory и работы с Telegram Bot API.

## Описание

Бот позволяет пользователям:
- рассчитывать график платежей по кредиту (аннуитетный или дифференцированный);
- просматривать историю своих запросов;
- (для менеджеров) получать агрегированную аналитику по всем запросам.

Данные о запросах хранятся в памяти (in-memory) и сбрасываются при остановке приложения.

## Архитектура

Проект разделён на слои с чётким разделением ответственности:
com.example.creditbot/
├── Main.java – точка входа, запуск бота
├── config/
│ └── BotConfig.java – настройки (токен, username, пароль админа)
├── bot/
│ ├── CreditBot.java – основной класс бота (обработка обновлений, делегирование команд)
│ ├── CommandHandler.java – интерфейс обработчика команды
│ └── handler/ – реализации обработчиков
│ ├── StartHandler.java
│ ├── CalculateHandler.java
│ ├── HistoryHandler.java
│ ├── AdminHandler.java
│ └── CancelHandler.java
├── calculator/
│ ├── PaymentCalculator.java – интерфейс калькулятора платежей
│ ├── AnnuityCalculator.java – аннуитентные платежи
│ ├── DifferentiatedCalculator.java – дифференцированные платежи
│ ├── CalculatorFactory.java – фабрика калькуляторов
│ └── CalculationSessionManager.java – управление пошаговым вводом параметров
├── model/
│ ├── LoanRequest.java – параметры кредита
│ ├── PaymentSchedule.java – результат расчёта
│ └── UserRequest.java – запись о запросе пользователя (для аналитики)
├── storage/
│ ├── RequestStorage.java – интерфейс хранилища
│ └── InMemoryRequestStorage.java – реализация на ArrayList
└── analytics/
└── AnalyticsService.java – агрегация и статистика


### Принципы SOLID

- **Single Responsibility** – каждый класс отвечает за свою задачу (калькуляторы, обработчики, хранилище).
- **Open/Closed** – новые типы платежей добавляются через реализацию интерфейса `PaymentCalculator`, без изменения существующего кода.
- **Liskov Substitution** – реализации интерфейсов (`AnnuityCalculator`, `InMemoryRequestStorage`) полностью подменяют базовые контракты.
- **Interface Segregation** – узкие интерфейсы (`CommandHandler`, `PaymentCalculator`, `RequestStorage`).
- **Dependency Inversion** – `CreditBot` и `CalculateHandler` зависят от абстракций, а не от конкретных реализаций.

### Низкая связанность и высокая связность

- Компоненты взаимодействуют через интерфейсы, что снижает связанность.
- Каждый класс сфокусирован на одной задаче, что повышает связность.

### Паттерн Factory

`CalculatorFactory` создаёт нужный калькулятор по типу платежа, скрывая детали создания.

## Требования

- Java 21 или выше
- Maven 3.8+
- Зарегистрированный Telegram-бот (получить токен у @BotFather)

## Настройка безопасного хранения токена

Токен не хранится в коде. Он читается из файла `config.properties`, который **не добавляется в Git** (см. `.gitignore`).

1. Создай файл `src/main/resources/config.properties` со следующим содержимым:

config.properties

bot.token=8998540141:AAEjNTWwuIG4GSxPhv1sRAqLwYC-XjkcRek
bot.username=creditJava_bot
admin.password=admin123
Убедись, что .gitignore содержит:

config.properties


Запуск
Склонируй репозиторий:

git clone <URL_РЕПОЗИТОРИЯ>
cd credit-bot
Помести config.properties в src/main/resources/ (см. выше).

Собери проект:

mvn clean package
Запусти бота:

java -jar target/credit-bot-1.0-SNAPSHOT.jar
Или через IDE: открой проект в IntelliJ IDEA и запусти класс Main.

После запуска бот начнёт принимать сообщения. Открой Telegram, найди своего бота и отправьте /start.

Команды
Пользовательские
Команда	Описание
/start	Приветственное сообщение и клавиатура
/calculate	Начать расчёт графика платежей. Можно передать параметры сразу: /calculate 100000 12 15 annuity или запустить пошаговый ввод.
/history	Показать историю своих запросов
/cancel	Отменить текущий пошаговый ввод
Для менеджеров
Команда	Описание
/admin admin123	Вывести агрегированную аналитику (количество запросов, средняя сумма, популярные типы платежей, распределение по месяцам)
Пароль по умолчанию: admin123.
