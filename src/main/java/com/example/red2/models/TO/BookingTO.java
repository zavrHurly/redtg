package com.example.red2.models.TO;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class BookingTO {

    private long userId;

    private String userName;

    private LocalTime startTime;

    private Date startDate;

    private LocalDateTime finishTime;

    private Integer person;

    private boolean ps;

    private Long duration;

    private String comment;

    public BookingTO(long userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

}

