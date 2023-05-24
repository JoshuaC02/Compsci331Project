package proj.concert.service.mapper;

import proj.concert.common.dto.BookingDTO;
import proj.concert.common.dto.SeatDTO;
import proj.concert.service.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookingMapper {
    public static Booking toDomainModel(BookingDTO bookingDto, User user, Concert concert) {
        List<SeatDTO> seatsDto = bookingDto.getSeats();

        List<Seat> seats = seatsDto.stream()
                .map(seatDto -> SeatMapper.toDomainModel(seatDto))
                .collect(Collectors.toList());

        Booking booking = new Booking(
                user,
                seats,
                concert,
                bookingDto.getDate()
        );

        return booking;
    }

    public static BookingDTO toDto(Booking booking) {
        List<Seat> seats = booking.getSeats();

        List<SeatDTO> seatsDto = seats.stream()
                .map(seat -> SeatMapper.toDto(seat))
                .collect(Collectors.toList());

        BookingDTO bookingDto = new BookingDTO(
                booking.getConcert().getId(),
                booking.getDate(),
                seatsDto
        );

        return bookingDto;
    }
}
