package com.example.red2.models;

import com.example.red2.models.to.BookingTO;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

import static com.example.red2.service.DateTimeHelper.convertDateToLocalDateTime;

@Entity
@Table(name = "Bookings")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Booking implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "userId")
    private long userId;

    @Column(name = "userName")
    private String userName;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "start")
    private LocalDateTime startTime;

    @Column(name = "finish")
    private LocalDateTime finishTime;

    @Column(name = "countPerson")
    private Integer countPerson;

    @Column(name = "ps")
    private boolean ps;

    @Column(name = "duration")
    private Long duration;

    @Column
    private String comment;

    public Booking(BookingTO to) {
        this.userId = to.getUserId();
        this.userName = to.getUserName();
        this.finishTime = to.getFinishTime();
        this.countPerson = to.getCountPerson();
        this.ps = to.isPs();
        this.duration = new Long (to.getDuration());
        this.comment = to.getComment();
        this.startTime= to.getStartTime().atDate(to.getStartDate());
        this.date = to.getStartDate();

    }

    public void setFinishTime(LocalTime plusHours) {

    }
}
