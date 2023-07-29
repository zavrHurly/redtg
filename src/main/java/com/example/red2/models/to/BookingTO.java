package com.example.red2.models.to;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class BookingTO {

    private long userId;

    private String userName;

    private LocalTime startTime;

    private LocalDate startDate;

    private LocalDateTime finishTime;

    private Integer countPerson;

    private boolean ps;

    private Integer duration;

    private String comment;

    private boolean finishStatus;

    public BookingTO(long userId, String userName) {
        this.userId = userId;
        this.userName = userName;
        finishStatus = false;
    }

    public void setCountPerson(String person) {
        try {
            countPerson = Integer.parseInt(person);
        } catch (NumberFormatException e) {
            countPerson = null;
        }
    }

    public void setDuration(String duration) {
        try {
            this.duration = Integer.parseInt(duration);
            if(this.duration > 10) this.duration = null;
        } catch (NumberFormatException e) {
            this.duration = null;
        }
    }

    public boolean getFinishStatus() {
        return finishStatus;
    }
}

