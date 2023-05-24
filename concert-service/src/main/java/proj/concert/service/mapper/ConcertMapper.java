package proj.concert.service.mapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import proj.concert.common.dto.ConcertDTO;
import proj.concert.common.dto.PerformerDTO;
import proj.concert.service.domain.Concert;
import proj.concert.service.domain.Performer;

public class ConcertMapper {
    public static Concert toDomainModel(ConcertDTO concertDto) {
        List<PerformerDTO> performersDto = concertDto.getPerformers();
        List<Performer> performers = performersDto.stream()
                .map(performerDto -> PerformerMapper.toDomainModel(performerDto))
                .collect(Collectors.toList());

        Set<LocalDateTime> dates = new HashSet<LocalDateTime>(concertDto.getDates());

        Concert concert = new Concert(
                concertDto.getId(),
                concertDto.getTitle(),
                concertDto.getImageName(),
                concertDto.getBlurb(),
                dates,
                performers
        );

        return concert;
    }

    public static ConcertDTO toDto(Concert concert) {
        ConcertDTO concertDto = new ConcertDTO(
                concert.getId(),
                concert.getTitle(),
                concert.getImageName(),
                concert.getBlurb()
        );

        List<LocalDateTime> dates = new ArrayList<LocalDateTime>(concert.getDates());
        concertDto.setDates(dates);

        List<Performer> performers = concert.getPerformers();
        List<PerformerDTO> performersDto = performers.stream()
                .map(performer -> PerformerMapper.toDto(performer))
                .collect(Collectors.toList());

        concertDto.setPerformers(performersDto);

        return concertDto;
    }
}
