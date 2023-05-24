package proj.concert.service.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "BOOKINGS")
public class Booking {
    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToMany
    private List<Seat> seats;

    @ManyToOne
    private Concert concert;

    private LocalDateTime date;

    public Booking(User user, List<Seat> seats, Concert concert, LocalDateTime date) {
        this.user = user;
        this.seats = seats;
        this.concert = concert;
        this.date = date;
    }

    public Booking() {}

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Seat> getSeats() {
        return this.seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public Concert getConcert() {
        return this.concert;
    }

    public void setConcert(Concert concert) {
        this.concert = concert;
    }

    public LocalDateTime getDate() {
        return this.date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
