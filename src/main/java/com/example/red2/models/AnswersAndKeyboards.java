package com.example.red2.models;

import java.util.Arrays;
import java.util.List;

public final class AnswersAndKeyboards {

    public static final String HELP_ANSWER = "Я нужен для того, чтобы узнать о наличии свободных мест в нашем заведении," +
                                            "еще я могу для тебя забронировать стол. Если ты будешь в ближайшие 20 минут," +
                                            "я могу попросить поставить для тебя угли";

    public static final String ERROR_ANSWER = "К сожалению я не могу понять, что ты мне написал, попробуй еще раз)";

    public static final String COAL_ANSWER = "Хорошо, как скоро тебя ждать?";

    public static final String BOOK_ANSWER_FOR_BUTTONS = "Нажми на одну из кнопок под сообщением, чтобы я понимал какой столик выбрать для тебя.";

    public static final String POSITIVE_RESULT_ANSWER = "Хорошо, до скорой встречи";

    public static final List<String> START_KEYBOARD = Arrays.asList("Поставить угли", "Забронировать стол");

    public static final List<String> COAL_KEYBOARD = Arrays.asList("5 минут", "10 минут", "15 минут", "20 минут");

    public static final List<String> HOURS_KEYBOARD = Arrays.asList("1 час", "2 часа", "3 часа", "4 часа", "5 часов", "6 часов");

    public static final String CREATE_BOOK_ANSWER = "Хорошо, сейчас кое-что уточню";

    public static final String NEGATIVE_ANSWER = "Я не могу тебя понять, попробуй еще раз";

    public static final String POSITIVE_BOOK_ANSWER_1 = "Напишите мне дату и время вашего посещения по примеру '20 02 16:00'Где 20-число, 02-месяц, 16:00-время";

    public static final String POSITIVE_BOOK_ANSWER_2 = "Сколько человек будет?";

    public static final String POSITIVE_BOOK_ANSWER_3 = "Как долго планируете у нас пребывать?";

    public static final String POSITIVE_BOOK_ANSWER_4 = "Спасибо большое, будем ждать вас в указанное время)";

    public static final String FINAL_POSITIVE_ANSWER = "Столик успешно забронирован";

    public static final String BUTTON_PS = "Стол с PS";

    public static final String BUTTON_DEFAULT = "Любой стол";

    public static final String BUTTONS_NAME_DEFAULT = "TABLE";

    public static final String BUTTONS_NAME_WITH_PS = "TABLE_WITH_PS";

    public static final long ADMIN_CHAT = 789903058;
}
