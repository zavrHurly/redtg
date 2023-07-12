package com.example.red2.service.creators;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static com.example.red2.models.AnswersAndKeyboards.*;
import static com.example.red2.models.AnswersAndKeyboards.BUTTONS_NAME_DEFAULT;

@Component
@NoArgsConstructor
public class KeyboardCreator {

    public ReplyKeyboardMarkup createKeyboard(List<String> buttons) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        ListIterator<String> listIterator = buttons.listIterator();
        while(listIterator.hasNext()) {
            KeyboardRow row = new KeyboardRow();
            if(listIterator.nextIndex() + 1 == buttons.size()) {
                row.add(listIterator.next());
                keyboardRows.add(row);
            } else {
                row.add(listIterator.next());
                row.add(listIterator.next());
                keyboardRows.add(row);
            }
        }
        keyboardMarkup.setKeyboard(keyboardRows);
        return keyboardMarkup;
    }

    public InlineKeyboardMarkup createMarkupInline() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        var nowButton = new InlineKeyboardButton();
        nowButton.setText(BUTTON_PS);
        nowButton.setCallbackData(BUTTONS_NAME_WITH_PS);
        var afterButton = new InlineKeyboardButton();
        afterButton.setText(BUTTON_DEFAULT);
        afterButton.setCallbackData(BUTTONS_NAME_DEFAULT);
        rowInline.add(nowButton);
        rowInline.add(afterButton);
        rowsInline.add(rowInline);
        keyboardMarkup.setKeyboard(rowsInline);
        return keyboardMarkup;
    }
}
