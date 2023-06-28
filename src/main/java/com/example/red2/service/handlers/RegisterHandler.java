package com.example.red2.service.handlers;

import com.example.red2.models.User;
import com.example.red2.repository.UserRepository;
import com.example.red2.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
@Slf4j
public class RegisterHandler extends AbstractHandler{

    private final UserService userService;

    @Autowired
    public RegisterHandler(UserRepository userRepository) {
        this.userService = new UserService(userRepository);
    }

    public boolean isProcessed(Update update) {
        if(update.hasMessage()) return userService.getById(update.getMessage().getChatId()) == null;
        return false;
    }

    public void processed(Update update) {
        if(userService.getById(update.getMessage().getChatId()) == null) {
            User newUser = new User(update.getMessage());
            userService.save(newUser);
            log.info("Сохранен пользователь {}", newUser);
        }
    }
}
