package com.example.red2.service.creators;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

@AllArgsConstructor
@Component
public class MessageCreator {

    private KeyboardAndInlineCreator keyboardAndInlineCreator;

    public SendMessage createMessageToAdmin(long chatId, String textToSend, String name, String userName) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(userName + "\n" + name + "\n" + textToSend);
        return message;
    }

    public SendMessage createMessageWithKeyboard(long chatId, String textToSend, List keyboard) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        if(keyboard != null) {
            message.setReplyMarkup(keyboardAndInlineCreator.createKeyboard(keyboard));
        }
        return message;
    }

    public SendMessage createMessageWithMarkupInline(long chatId, String textToSend, boolean markupInline) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        if(markupInline) {
            message.setReplyMarkup(keyboardAndInlineCreator.createMarkupInline());
        }
        return message;
    }
}
