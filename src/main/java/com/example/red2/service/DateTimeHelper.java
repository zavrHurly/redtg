package com.example.red2.service;

import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;

@NoArgsConstructor
public class DateTimeHelper {

    public LocalTime messageToTime(String message) throws NullPointerException {
        String [] timer = message.split("[^0-9]");
        String time = null;
        try {
            if (Integer.parseInt(timer[0]) > 1 && Integer.parseInt(timer[0]) < 16) return null;
            if(Integer.parseInt(timer[0]) > 23 | Integer.parseInt(timer[1]) > 59) return null;
            if (timer.length == 2) time = (timer[0] + ":" + timer[1] + ":" + "00");
        } catch (ArrayIndexOutOfBoundsException | NullPointerException | DateTimeException e) {
            return null;
        }
        return LocalTime.parse(time);
    }

    public static LocalDateTime convertDateToLocalDateTime(Date dateToConvert, LocalTime time) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime().withHour(time.getHour()).withMinute(time.getMinute());
    }

    public Date createDateVisit(String message, long userId) throws TelegramApiException, ParseException {
        LocalDate today = LocalDate.now();
        if(message.equals("Сегодня")) {
            return Date.from(today.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        } else if(message.equals("Завтра")) {
            return Date.from(today.plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        } else {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
                return formatter.parse(message);
            } catch (ParseException e) {
                return null;
            }
        }
    }
}
