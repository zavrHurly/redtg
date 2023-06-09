package com.example.red2.models;

import com.example.red2.models.TO.BookingTO;
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
@Getter
@Setter
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

    @Column(name = "person")
    private Integer person;

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
        this.person = to.getPerson();
        this.ps = to.isPs();
        this.duration = to.getDuration();
        this.comment = to.getComment();
        this.startTime= convertDateToLocalDateTime(to.getStartDate(), to.getStartTime());
        this.date = to.getStartDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

    }

    public void setFinishTime(LocalTime plusHours) {

    }
}
