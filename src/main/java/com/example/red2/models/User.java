package com.example.red2.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.persistence.Column;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    @Id
    @Column(name = "id")
    public Long id;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "userName")
    private String userName;

    @Column(name = "registration")
    private Timestamp registrationTime;

    @Column(name = "action")
    private boolean actionBook;

    public User(Message msg) {
        id = msg.getChatId();
        firstName = msg.getChat().getFirstName();
        lastName = msg.getChat().getLastName();
        userName = msg.getChat().getUserName();
        registrationTime = new Timestamp(System.currentTimeMillis());

    }
}
