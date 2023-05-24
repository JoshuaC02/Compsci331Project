package proj.concert.service.mapper;

import proj.concert.common.dto.SeatDTO;
import proj.concert.service.domain.Seat;

import java.math.BigDecimal;


public class SeatMapper {
    public static Seat toDomainModel(SeatDTO seatDto) {
        Seat concert = new Seat(
                seatDto.getLabel(),
                false,
                null,
                seatDto.getPrice()
        );

        return concert;
    }

    public static SeatDTO toDto(Seat seat) {
        SeatDTO seatDto = new SeatDTO(
                seat.getLabel(),
                seat.getPrice()
        );

        return seatDto;
    }
}
