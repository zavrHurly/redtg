package com.example.red2.service;

import com.example.red2.models.User;
import com.example.red2.repository.UserRepository;
import com.vdurmont.emoji.EmojiParser;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static com.example.red2.models.AnswersAndKeyboards.*;
import static com.example.red2.models.AnswersAndKeyboards.BUTTONS_NAME_DEFAULT;

@Service
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class BotHelper {

    @Autowired
    private UserRepository repository;



    public void registerUser(Message message) {
        if(repository.findById(message.getChatId()).isEmpty()) {
            User newUser = new User(message);
            repository.save(newUser);
            log.info("Сохранен пользователь {}", newUser);
        }
    }

    public List<BotCommand> createBotMenu() {
        List<BotCommand> commands = new ArrayList<>();
        commands.add(new BotCommand("/start", "запуск"));
        commands.add(new BotCommand("/help", "описание"));
        return commands;
    }

    public SendMessage createAnswer(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String userName = update.getMessage().getChat().getFirstName();

            switch (message) {
                case "/start":
                    registerUser(update.getMessage());
                    return startCommand(chatId, userName);
                case "/help":
                    return help(chatId, userName);
                case "Поставить угли":
                    return createMessageWithKeyboard(chatId, COAL_ANSWER, COAL_KEYBOARD);
                case "Забронировать стол":
                    return createMessageWithMarkupInline(chatId, BOOK_ANSWER, true);
                default:
                    return createMessageWithKeyboard(update.getMessage().getChatId(), ERROR_ANSWER, START_KEYBOARD);
            }
        } else if(update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            EditMessageText messageText = new EditMessageText();
            messageText.setChatId(String.valueOf(chatId));
            messageText.setMessageId((int) messageId);
            if(callbackData.equals(BUTTONS_NAME_DEFAULT)) {
                messageText.setText("Хорошо, посмотрю, есть ли свободные столики");
                bookATable(update);
            } else if(callbackData.equals(BUTTONS_NAME_WITH_PS)) {
                messageText.setText("Хорошо, посмотрю, есть ли свободные приставки");
                bookATablePS(update);
            }
        }
        return null;
    }

    public SendMessage startCommand(long chatId, String name) {
        String answer = EmojiParser.parseToUnicode("Привет, " + name + " , чем я могу тебе помочь?" + " :blush:");
        log.info("Replied to user:" + name);
        return createMessageWithKeyboard(chatId, answer, START_KEYBOARD);
    }

    public SendMessage help(long chatId, String name) {
        log.info("Replied to user:" + name);
        return createMessageWithKeyboard(chatId, HELP_ANSWER, START_KEYBOARD);
    }

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

    InlineKeyboardMarkup createMarkupInline() {
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
            message.setReplyMarkup(createKeyboard(keyboard));
        }
        return message;
    }

    public SendMessage createMessageWithMarkupInline(long chatId, String textToSend, boolean markupInline) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        if(markupInline) {
            message.setReplyMarkup(createMarkupInline());
        }
        return message;
    }

    public List<SendMessage> putTheCoals(Update update) {
        List<SendMessage> messages = new ArrayList<>();
            messages.add(createMessageToAdmin(ADMIN_CHAT, update.getMessage().getText(), update.getMessage().getChat().getFirstName(), update.getMessage().getChat().getUserName()));
            messages.add(createMessageWithKeyboard(update.getMessage().getChatId(), POSITIVE_RESULT_ANSWER, START_KEYBOARD));
            return messages;
    }

    public boolean checkingForThePresenceOfTimeInTheText(Update update) {
        return update.hasMessage() && update.getMessage().getText().contains("минут") || update.getMessage().getText().contains("час");
    }

    public void bookATable(Update update) {

    }

    public void bookATablePS(Update update) {

    }
}
