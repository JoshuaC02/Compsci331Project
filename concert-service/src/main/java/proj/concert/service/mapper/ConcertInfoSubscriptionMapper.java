package proj.concert.service.mapper;

import proj.concert.common.dto.ConcertInfoSubscriptionDTO;
import proj.concert.service.domain.ConcertInfoSubscription;
import proj.concert.service.domain.User;

import java.util.List;
import java.util.stream.Collectors;

public class ConcertInfoSubscriptionMapper {
    public static ConcertInfoSubscription toDomainModel(ConcertInfoSubscriptionDTO subDto, User user) {

        ConcertInfoSubscription subInfo = new ConcertInfoSubscription(
                user,
                subDto.getConcertId(),
                subDto.getDate(),
                subDto.getPercentageBooked()
        );

        return subInfo;
    }

    public static ConcertInfoSubscriptionDTO toDto(ConcertInfoSubscription subInfo) {

        ConcertInfoSubscriptionDTO subDto = new ConcertInfoSubscriptionDTO(
                subInfo.getConcertId(),
                subInfo.getDate(),
                subInfo.getPercentageBooked()
        );

        return subDto;
    }
}
