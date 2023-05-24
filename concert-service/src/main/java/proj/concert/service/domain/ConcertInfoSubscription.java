package proj.concert.service.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "SUBSCRIBTIONS")
public class ConcertInfoSubscription {
    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    private Long id;

    @ManyToOne
    private User user;

    @Column(name = "CONCERT_ID", nullable = false)
    private long concertId;

    private LocalDateTime date;

    @Column(name = "PERCENTAGE", nullable = false)
    private int percentageBooked;

    public ConcertInfoSubscription() {}

    public ConcertInfoSubscription(User user, long concertId, LocalDateTime date, int percentageBooked) {
        this.user = user;
        this.concertId = concertId;
        this.date = date;
        this.percentageBooked = percentageBooked;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getConcertId() {
        return concertId;
    }

    public void setConcertId(long concertId) {
        this.concertId = concertId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getPercentageBooked() {
        return percentageBooked;
    }

    public void setPercentageBooked(int percentageBooked) {
        this.percentageBooked = percentageBooked;
    }
}
