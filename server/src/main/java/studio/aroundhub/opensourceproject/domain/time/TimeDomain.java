package studio.aroundhub.opensourceproject.domain.time;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;

@Entity
@Getter
@Setter
@Table(name="timedomain")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class TimeDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name  ="id",updatable = false,nullable = false)
    private long id;

    @Column(name = "email",updatable = false,nullable = false)
    private String email;

    @Column(name="subject_name",nullable = false)
    private String subjectName;

    @Column(name="day_of_week",nullable = false)
    private String dayOfWeek;


    @Column(name= "startTime",nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private Time startTime;

    @Column(name= "endTime",nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private Time endTime;

    @Builder
    public TimeDomain(String email, String subjectName, String dayOfWeek, Time startTime, Time endTime) {
        this.email = email;
        this.subjectName = subjectName;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }



}

