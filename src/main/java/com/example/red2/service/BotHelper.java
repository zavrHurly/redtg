package com.example.red2.service;

import com.example.red2.models.Book;
import com.example.red2.models.User;
import com.example.red2.repository.BookRepository;
import com.example.red2.repository.UserRepository;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import static com.example.red2.models.AnswersAndKeyboards.*;
import static com.example.red2.models.AnswersAndKeyboards.BUTTONS_NAME_DEFAULT;

@Component
@Slf4j
public class BotHelper {

    private final UserService userService;

    private final BookService bookService;

    private HashMap<Long, Book> bookInProcessing = new HashMap<>();

    @Autowired
    public BotHelper(UserRepository userRepository, BookRepository bookRepository) {
        this.userService = new UserService(userRepository);
        this.bookService = new BookService(bookRepository);
    }



    public void registerUser(Message message) {
        if(userService.getById(message.getChatId()) == null) {
            User newUser = new User(message);
            userService.save(newUser);
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
        String message = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();
        String userName = update.getMessage().getChat().getFirstName();

        switch (message) {
            case "/start":
                    return startCommand(chatId, userName);
            case "/help":
                return help(chatId, userName);
            case "Поставить угли":
                return createMessageWithKeyboard(chatId, COAL_ANSWER, COAL_KEYBOARD);
            case "Забронировать стол":
                return createMessageWithMarkupInline(chatId, BOOK_ANSWER_FOR_BUTTONS, true);
            default:
                return createMessageWithKeyboard(update.getMessage().getChatId(), ERROR_ANSWER, START_KEYBOARD);
            }
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
        if(update.hasMessage()) return update.getMessage().getText().contains("минут") || update.getMessage().getText().contains("час");
        return false;
    }

    public boolean checkingForAButton(Update update) {
        return update.hasCallbackQuery();
    }

    public boolean textPresenceCheck(Update update) {
        return update.hasMessage() & update.getMessage().hasText();
    }

    public EditMessageText createAButtonResponse(Update update) {
        String callbackData = update.getCallbackQuery().getData();
        EditMessageText messageText = new EditMessageText();
        messageText.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
        messageText.setMessageId((int) update.getCallbackQuery().getMessage().getMessageId());
        if (callbackData.equals(BUTTONS_NAME_DEFAULT)) {
            messageText.setText(CREATE_BOOK_ANSWER);
            bookATable(update);
        } else if (callbackData.equals(BUTTONS_NAME_WITH_PS)) {
            messageText.setText(CREATE_BOOK_ANSWER);
            bookATablePS(update);
        }
        return messageText;
    }

    public boolean bookingAvailabilityCheck(Update update) {
        if(update.hasMessage()) {
            return userService.getAction(update.getMessage().getChatId());
        } else {
            return userService.getAction(update.getCallbackQuery().getMessage().getChatId());
        }

    }

    public void bookATable(Update update) {
        userService.setAction(update.getCallbackQuery().getMessage().getChatId(), true);
    }

    public void bookATablePS(Update update) {
        userService.setAction(update.getCallbackQuery().getMessage().getChatId(), true);
    }

    public SendMessage createBook(Update update) {
        long userId = update.getMessage().getChatId();
        Book book = bookInProcessing.get(userId);
        if(!bookInProcessing.containsKey(userId)) {
            bookInProcessing.put(userId, new Book(update.getMessage().getChatId(), update.getMessage().getChat().getUserName()));
            return createMessageWithKeyboard(userId, POSITIVE_BOOK_ANSWER_1, null);
        } else if(book.getStartTime() == null) {
            book.setStartTime(messageToDateTime(update.getMessage().getText()));
            bookInProcessing.put(userId, book);
            return createMessageWithKeyboard(userId, POSITIVE_BOOK_ANSWER_2, null);
        } else if(book.getPerson() == null) {
            book.setPerson(Integer.parseInt(update.getMessage().getText()));
            bookInProcessing.put(userId, book);
            return createMessageWithKeyboard(userId, POSITIVE_BOOK_ANSWER_3, HOURS_KEYBOARD);
        } else if(book.getDuration() == null) {
            book.setDuration(hoursInMessage(update.getMessage().getText()));
            book.setFinishTime(book.getStartTime().plusHours(hoursInMessage(update.getMessage().getText())));
            bookInProcessing.put(userId, book);
            bookService.save(book);
            userService.setAction(userId, false);
            return createMessageWithKeyboard(userId, POSITIVE_BOOK_ANSWER_4, null);
        }
        return createMessageWithKeyboard(userId, NEGATIVE_ANSWER, null);
    }

    private LocalDateTime messageToDateTime(String message) {
        LocalDateTime startTime = LocalDateTime.now();
        String [] timer = message.split("[^0-9]");
        String dataTime = (startTime.getYear() +"-" + timer[1] + "-" + timer[0] + "T" + timer[2] + ":" + timer[3] + ":" + "00" );
        return LocalDateTime.parse(dataTime);
    }

    private long hoursInMessage(String message) {
        String[] hourAndText = message.split(" ");
        return Long.parseLong(hourAndText[0]);
    }
}
