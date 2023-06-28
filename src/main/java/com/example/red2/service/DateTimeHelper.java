package com.example.red2.service;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

import static com.example.red2.models.AnswersAndKeyboards.POSITIVE_BOOK_ANSWER_4;

public class DateTimeHelper {

    public LocalTime messageToTime(String message) {
        String [] timer = message.split("[^0-9]");
        String time = null;
        if(timer.length > 1) time = (timer[0] + ":" + timer[1] + ":" + "00" );
        return LocalTime.parse(time);
    }

    private LocalTime hoursInMessage(String message) {
        return LocalTime.parse(message + ":00:00");
    }

    public static LocalDateTime convertDateToLocalDateTime(Date dateToConvert, LocalTime time) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime().withHour(time.getHour()).withMinute(time.getMinute());
    }
}
