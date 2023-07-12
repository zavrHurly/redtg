package com.example.red2.models.TO;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

import static com.example.red2.service.DateTimeHelper.convertDateToLocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@ToString
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

    public void setPerson(String person) {
        try {
            this.person = Integer.parseInt(person);
        } catch (NumberFormatException e) {
            this.person = null;
        }
    }

    public void setDuration(String duration) {
        try {
            this.duration = Long.parseLong(duration);
            if(this.duration > 10) this.duration = null;
        } catch (NumberFormatException e) {
            this.duration = null;
        }
    }

}

