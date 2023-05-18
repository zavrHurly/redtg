package com.example.red2.service;

import com.example.red2.config.BotConfig;
import com.example.red2.models.User;
import com.example.red2.repository.UserRepository;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static com.example.red2.models.AnswersAndKeyboards.*;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private UserRepository repository;
    final BotConfig botConfig;

    public TelegramBot(BotConfig botConfig) {
        this.botConfig = botConfig;
        List<BotCommand> commands = createBotMenu();
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String userName = update.getMessage().getChat().getFirstName();

            switch (message) {
                case "/start":
                    registerUser(update.getMessage());
                    startCommand(chatId, userName);
                    break;
                case "/help":
                    help(chatId, userName);
                    break;
                case "Поставить угли":
                    sendMessage(chatId, COAL_ANSWER, COAL_KEYBOARD);
                    break;
                case "Забронировать стол":
                    sendMessage(chatId, BOOK_ANSWER, true);
                    break;
                default:
                    putTheCoals(update);
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
            try {
                execute(messageText);
            } catch (TelegramApiException e) {
                log.error("Error occurred: " + e.getMessage());
            }
        }
    }

    private List<BotCommand> createBotMenu() {
        List<BotCommand> commands = new ArrayList<>();
        commands.add(new BotCommand("/start", "запуск"));
        commands.add(new BotCommand("/help", "описание"));
        try{
            this.execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting command list: " + e);
        }
        return commands;
    }

    private void startCommand(long chatId, String name) {
        String answer = EmojiParser.parseToUnicode("Привет, " + name + " , чем я могу тебе помочь?" + " :blush:");
        log.info("Replied to user:" + name);
        sendMessage(chatId, answer, START_KEYBOARD);
    }

    private void help(long chatId, String name) {
        log.info("Replied to user:" + name);
        sendMessage(chatId, HELP_ANSWER, START_KEYBOARD);
    }

    private void sendMessage(long chatId, String textToSend, List keyboard) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        if(keyboard != null) {
            message.setReplyMarkup(createKeyboard(keyboard));
        }
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }

    private void sendMessage(long chatId, String textToSend, boolean markupInline) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        if(markupInline) {
            message.setReplyMarkup(createMarkupInline());
        }
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }

    private void sendMessageToAdmin(long chatId, String textToSend, String name, String userName) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(userName + "\n" + name + "\n" + textToSend);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }

    private void registerUser(Message message) {
        if(repository.findById(message.getChatId()).isEmpty()) {
            User newUser = new User(message);
            repository.save(newUser);
            log.info("Сохранен пользователь {}", newUser);
        }
    }

    private ReplyKeyboardMarkup createKeyboard(List<String> buttons) {
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

    private InlineKeyboardMarkup createMarkupInline() {
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

    private void putTheCoals(Update update) {
        if (update.hasMessage() && update.getMessage().getText().contains("минут") || update.getMessage().getText().contains("час")) {
            sendMessageToAdmin(ADMIN_CHAT, update.getMessage().getText(), update.getMessage().getChat().getFirstName(), update.getMessage().getChat().getUserName());
            sendMessage(update.getMessage().getChatId(), POSITIVE_RESULT_ANSWER, START_KEYBOARD);
        } else {
            sendMessage(update.getMessage().getChatId(), ERROR_ANSWER, START_KEYBOARD);
        }
    }

    private void bookATable(Update update) {

    }

    private void bookATablePS(Update update) {

    }
}
