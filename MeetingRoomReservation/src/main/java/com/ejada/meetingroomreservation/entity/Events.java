package com.ejada.meetingroomreservation.entity;

import com.ejada.meetingroomreservation.Enums.EventStatus;
import com.ejada.meetingroomreservation.Enums.EventType;
import com.ejada.meetingroomreservation.utils.LocalDateAttributeConverter;
import com.ejada.meetingroomreservation.utils.LocalDateTimeAttributeConverter;
import com.ejada.meetingroomreservation.utils.LocalTimeAttributeConverter;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "EVENT")
@Data
public class Events implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "event_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User creator;

    @Column(name = "contact_person")
    private String contactPerson;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "location")
    private String location;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    private EventType type;

    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @Column(name = "event_date", nullable = false, columnDefinition = "DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Convert(converter = LocalDateAttributeConverter.class)
    private LocalDate eventDate;

    @Column(name = "start_at", nullable = false, columnDefinition = "TIME")
    @Convert(converter = LocalTimeAttributeConverter.class)
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startAt;

    @Column(name = "end_at", nullable = false, columnDefinition = "TIME")
    @Convert(converter = LocalTimeAttributeConverter.class)
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endAt;

    @Column(name = "creation_time", columnDefinition = "TIMESTAMP")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime creationTime;

    @PrePersist
    protected void onCreate() {
        if (this.creationTime == null) {
            this.creationTime = LocalDateTime.now();
        }
    }

}
