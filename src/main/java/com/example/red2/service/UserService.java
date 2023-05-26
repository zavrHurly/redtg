package com.example.red2.service;

import com.example.red2.models.Book;
import com.example.red2.models.User;
import com.example.red2.repository.BookRepository;
import com.example.red2.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User update(User user, Long id) {
        User userFromDB = userRepository.getById(id);
        BeanUtils.copyProperties(user, userFromDB, "id");
        return user;
    }

    public boolean getAction(Long id) {
        return userRepository.getAction(id);
    }

    @Transactional
    public User setAction(Long id, boolean action) {
        User user = userRepository.getById(id);
        user.setAction(action);
        userRepository.save(user);
        return user;
    }

    public User getById(Long id) {
        return userRepository.getById(id);
    }

    public User save(User user) {
        userRepository.save(user);
        return user;
    }
}
