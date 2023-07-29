package com.example.red2.service.creators;

import com.example.red2.models.Booking;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

@Component
public class MessageCreator {

    private KeyboardCreator keyboardAndInlineCreator;

    public MessageCreator(){
        keyboardAndInlineCreator = new KeyboardCreator();
    }

    public SendMessage createMessageToAdmin(long chatId, String textToSend, String name, String userName) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(userName + "\n" + name + "\n" + textToSend);
        return message;
    }

    public SendMessage createMessageToAdmin(long chatId, Booking booking) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(booking.toString());
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
}
