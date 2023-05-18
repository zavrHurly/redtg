package com.example.red2.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "books")
@AllArgsConstructor
@NoArgsConstructor
public class Book implements Serializable {

    @Id
    private long id;

    @Column(name = "userName")
    private String userName;

    @Column(name = "start")
    private LocalDateTime startTime;

    @Column(name = "finish")
    private LocalDateTime finishTime;

    @Column(name = "person")
    private int person;

    @Column(name = "ps")
    private boolean ps;

    @Column(name = "duration")
    private LocalTime duration;
}
