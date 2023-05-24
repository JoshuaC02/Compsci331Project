package proj.concert.service.services;

import proj.concert.common.dto.*;
import proj.concert.common.types.BookingStatus;
import proj.concert.service.domain.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.OptimisticLockException;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Cookie;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import proj.concert.service.jaxrs.LocalDateTimeParam;
import proj.concert.service.mapper.*;


@Path("/concert-service")
public class ConcertResource {

    private static Logger LOGGER = LoggerFactory.getLogger(ConcertResource.class);

    private static String AUTH_COOKIE = "auth";

    private static ExecutorService threadPool = Executors.newSingleThreadExecutor();

    private static Map<Long, AsyncResponse> asyncResponses = new HashMap<>();

    @GET
    @Path("/concerts/{id}")
    @Produces({MediaType.APPLICATION_JSON, SerializationMessageBodyReaderAndWriter.APPLICATION_JAVA_SERIALIZED_OBJECT})
    public Response getConcert(@PathParam("id") Long id) {
        LOGGER.debug("Start GET /concerts/{id}");

        // Acquire an EntityManager (creating a new persistence context).
        EntityManager em = PersistenceManager.instance().createEntityManager();

        Concert concert;
        try {
            // Start a new transaction.
            em.getTransaction().begin();

            // Retrieve the Concert with the specified id.
            concert = em.find(Concert.class, id);

            // Commit the transaction.
            em.getTransaction().commit();
        } finally {
            // Close EntityManager to free up resources.
            em.close();
        }

        if (concert == null) {
            return Response.status(Response.Status.NOT_FOUND).build(); // 404 Not Found
        }

        ConcertDTO concertDTO = ConcertMapper.toDto(concert);
        return Response.ok(concertDTO).build();
    }

    @GET
    @Path("/concerts")
    @Produces({MediaType.APPLICATION_JSON, SerializationMessageBodyReaderAndWriter.APPLICATION_JAVA_SERIALIZED_OBJECT})
    public Response getConcerts() {
        LOGGER.debug("Start GET /concerts");

        // Acquire an EntityManager (creating a new persistence context).
        EntityManager em = PersistenceManager.instance().createEntityManager();

        // Initialise lists
        List<Concert> concerts;
        List<ConcertDTO> concertsReturn = new ArrayList<>();

        try {
            // Start a new transaction.
            em.getTransaction().begin();

            // Retrieve all the Concerts.
            concerts = em.createQuery("FROM Concert", Concert.class).getResultList();
            // Commit the transaction.
            em.getTransaction().commit();
        } finally {
            // Close EntityManager to free up resources.
            em.close();
        }

        // Return 404 if no concerts saved.
        if (concerts.size() == 0) {
            return Response.status(Response.Status.NOT_FOUND).build(); // 404 Not Found
        }

        // Change Concert into ConcertDTO for webapp.
        for (Concert concert : concerts) {
            ConcertDTO concertDTO = ConcertMapper.toDto(concert);
            concertsReturn.add(concertDTO);
        }

        // Return ConcertDTO list for webapp.
        return Response.ok(concertsReturn).build();
    }

    @GET
    @Path("/concerts/summaries")
    @Produces({MediaType.APPLICATION_JSON, SerializationMessageBodyReaderAndWriter.APPLICATION_JAVA_SERIALIZED_OBJECT})
    public Response getConcertSummaries() {
        LOGGER.debug("Start GET /concerts/summaries");

        // Acquire an EntityManager (creating a new persistence context).
        EntityManager em = PersistenceManager.instance().createEntityManager();

        // Initialise lists
        List<Concert> concerts;
        List<ConcertSummaryDTO> concertsReturn = new ArrayList<>();

        try {
            // Start a new transaction.
            em.getTransaction().begin();

            // Retrieve all the Concerts.
            concerts = em.createQuery("FROM Concert", Concert.class).getResultList();
            // Commit the transaction.
            em.getTransaction().commit();
        } finally {
            // Close EntityManager to free up resources.
            em.close();
        }

        // Return 404 if no concerts saved.
        if (concerts.size() == 0) {
            return Response.status(Response.Status.NOT_FOUND).build(); // 404 Not Found
        }

        // Change Concert into ConcertDTO for webapp.
        for (Concert concert : concerts) {
            ConcertSummaryDTO concertSummaryDTO = new ConcertSummaryDTO(concert.getId(), concert.getTitle(), concert.getImageName());
            concertsReturn.add(concertSummaryDTO);
        }

        // Return ConcertDTO list for webapp.
        return Response.ok(concertsReturn).build();
    }

    @GET
    @Path("/performers/{id}")
    @Produces({MediaType.APPLICATION_JSON, SerializationMessageBodyReaderAndWriter.APPLICATION_JAVA_SERIALIZED_OBJECT})
    public Response getPerformer(@PathParam("id") Long id) {
        LOGGER.debug("Start GET /performers/{id}");

        // Acquire an EntityManager (creating a new persistence context).
        EntityManager em = PersistenceManager.instance().createEntityManager();

        Performer performer;
        try {
            // Start a new transaction.
            em.getTransaction().begin();

            // Retrieve the Performer with the specified id.
            performer = em.find(Performer.class, id);

            // Commit the transaction.
            em.getTransaction().commit();
        } finally {
            // Close EntityManager to free up resources.
            em.close();
        }

        if (performer == null) {
            return Response.status(Response.Status.NOT_FOUND).build(); // 404 Not Found
        }

        PerformerDTO performerDto = PerformerMapper.toDto(performer);
        return Response.ok(performerDto).build();
    }

    @GET
    @Path("/performers")
    @Produces({MediaType.APPLICATION_JSON, SerializationMessageBodyReaderAndWriter.APPLICATION_JAVA_SERIALIZED_OBJECT})
    public Response getPerformers() {
        LOGGER.debug("Start GET /performers");

        // Acquire an EntityManager (creating a new persistence context).
        EntityManager em = PersistenceManager.instance().createEntityManager();

        // Initialise lists
        List<Performer> performers;
        List<PerformerDTO> performersReturn = new ArrayList<>();

        try {
            // Start a new transaction.
            em.getTransaction().begin();

            // Retrieve all the Performers.
            performers = em.createQuery("FROM Performer", Performer.class).getResultList();
            // Commit the transaction.
            em.getTransaction().commit();
        } finally {
            // Close EntityManager to free up resources.
            em.close();
        }

        // Return 404 if no concerts saved.
        if (performers.size() == 0) {
            return Response.status(Response.Status.NOT_FOUND).build(); // 404 Not Found
        }

        // Change Performer into PerformerDTO for webapp.
        for (Performer performer : performers) {
            PerformerDTO performerDTO = PerformerMapper.toDto(performer);
            performersReturn.add(performerDTO);
        }

        // Return PerformerDTO list for webapp.
        return Response.ok(performersReturn).build();
    }

    @POST
    @Path("/login")
    @Consumes({MediaType.APPLICATION_JSON, SerializationMessageBodyReaderAndWriter.APPLICATION_JAVA_SERIALIZED_OBJECT})
    public Response login(UserDTO userDto) {
        LOGGER.debug("Start POST /login; username = " + userDto.getUsername());

        // Acquire an EntityManager (creating a new persistence context).
        EntityManager em = PersistenceManager.instance().createEntityManager();

        User user;
        // Generate new cookie and return with response
        NewCookie cookie = new NewCookie(AUTH_COOKIE, UUID.randomUUID().toString());

        try {
            // Start a new transaction.
            em.getTransaction().begin();

            // Retrieve the User with the specified username
            user = em.createQuery("FROM User u WHERE u.username = :username", User.class).setParameter("username", userDto.getUsername()).getSingleResult();

            // Check User Credentials
            if (!(userDto.getPassword().equals(user.getPassword()))) {
                shutdownEM(em);
                return Response.status(Response.Status.UNAUTHORIZED).build(); // 401 Unauthorized
            }

            user.setCookie(cookie.toCookie().getValue());


            em.merge(user);

            // Commit the transaction.
            em.getTransaction().commit();
        } catch (javax.persistence.NoResultException E) {
            // Catches unknown username
            shutdownEM(em);
            return Response.status(Response.Status.UNAUTHORIZED).build(); // 401 Unauthorized
        } finally {
            // Close EntityManager to free up resources.
            em.close();
        }
        LOGGER.debug("=== End POST /login ===");
        return Response.ok().cookie(cookie).build();
    }

    @GET
    @Path("/seats/{date}")
    @Produces({MediaType.APPLICATION_JSON, SerializationMessageBodyReaderAndWriter.APPLICATION_JAVA_SERIALIZED_OBJECT})
    public Response getSeats(@PathParam("date") LocalDateTimeParam dateParam, @QueryParam("status") BookingStatus status) {
        LOGGER.debug("Start GET /seats/{date}");

        // Acquire an EntityManager (creating a new persistence context).
        EntityManager em = PersistenceManager.instance().createEntityManager();

        List<Seat> seats;
        try {
            // Start a new transaction.
            em.getTransaction().begin();

            LOGGER.debug(status.toString());
            String query = "FROM Seat WHERE date = :date";
            if (status.equals(BookingStatus.Booked)) {
                LOGGER.debug("Booked");
                query += " AND isBooked = true";
            }
            if (status.equals(BookingStatus.Unbooked)) {
                LOGGER.debug("Unbooked");
                query += " AND isBooked = false";
            }

            // Retrieve the Seat with the specified id.
            seats = em.createQuery(query, Seat.class).setParameter("date", dateParam.getLocalDateTime()).getResultList();

            // Commit the transaction.
            em.getTransaction().commit();
        } finally {
            // Close EntityManager to free up resources.
            em.close();
        }

        List<SeatDTO> seatsDto = new ArrayList<>();

        for (Seat seat : seats) {
            SeatDTO seatDto = SeatMapper.toDto(seat);
            seatsDto.add(seatDto);
        }
        LOGGER.debug("return seats");
        return Response.ok(seatsDto).build();
    }

    @POST
    @Path("/bookings")
    @Consumes({MediaType.APPLICATION_JSON, SerializationMessageBodyReaderAndWriter.APPLICATION_JAVA_SERIALIZED_OBJECT})
    public Response makeBooking(BookingRequestDTO bReq, @CookieParam("auth") Cookie auth) {
        LOGGER.debug("Start POST /bookings");
        // Test to see if User is logged in.
        if (auth == null) {
            LOGGER.debug("auth null");
            return Response.status(Response.Status.UNAUTHORIZED).build(); // 401 Unauthorized
        }
        LOGGER.debug("auth passed");

        // Acquire an EntityManager (creating a new persistence context).
        EntityManager em = PersistenceManager.instance().createEntityManager();
        LocalDateTime date = bReq.getDate();
        Booking booking;

        try {
            // Start a new transaction.
            em.getTransaction().begin();
            User user = em.createQuery("FROM User u WHERE u.cookie = :cookie", User.class).setParameter("cookie", auth.getValue()).getSingleResult();

            if (user == null) {
                LOGGER.debug("user null");
                shutdownEM(em);
                return Response.status(Response.Status.UNAUTHORIZED).build(); // 401 Unauthorized
            }

            Concert concert = em.find(Concert.class, bReq.getConcertId());

            if (concert == null) {
                LOGGER.debug("concert null");
                shutdownEM(em);
                return Response.status(Response.Status.BAD_REQUEST).build(); // 400 Bad Request
            }

            if (concert.getDates().stream().noneMatch(d -> d.equals(date))) {
                LOGGER.debug("date not in concert");
                shutdownEM(em);
                return Response.status(Response.Status.BAD_REQUEST).build(); // 400 Bad Request
            }

            List<Seat> seats = new ArrayList<>();
            for (String seatLabel : bReq.getSeatLabels()) {
                Seat seat = em.createQuery("FROM Seat s WHERE s.label = :label AND s.date = :date", Seat.class)
                    .setParameter("label", seatLabel)
                    .setParameter("date", date)
                    .setLockMode(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
                    .getSingleResult();

                if (seat == null) {
                    LOGGER.debug("seat null");
                    shutdownEM(em);
                    return Response.status(Response.Status.NOT_FOUND).build(); // 404 Not Found
                }

                if (seat.getIsBooked()) {
                    LOGGER.debug("seat is already booked");
                    shutdownEM(em);
                    return Response.status(Response.Status.FORBIDDEN).build(); // 403 Forbidden
                }

                seat.setIsBooked(true);
                seats.add(seat);
            }


            booking = new Booking(user, seats, concert, date);
            em.persist(booking);

            em.getTransaction().commit();
        } catch (OptimisticLockException E) {
            shutdownEM(em);
            return Response.status(Response.Status.FORBIDDEN).build();
        } finally {
            // Close EntityManager to free up resources.
            em.close();
        }

        this.checkSubscriptions(date);

        LOGGER.debug("=== End POST /bookings ===");
        return Response.created(URI.create("/concert-service/bookings/" + booking.getId())).build();
    }

    @GET
    @Path("/bookings/{id}")
    @Produces({MediaType.APPLICATION_JSON, SerializationMessageBodyReaderAndWriter.APPLICATION_JAVA_SERIALIZED_OBJECT})
    public Response getBooking(@PathParam("id") Long id, @CookieParam("auth") Cookie auth) {
        LOGGER.debug("Start GET /bookings/" + id);

        BookingDTO bookingDto;

        // Test to see if User is logged in.
        if (auth == null) {
            LOGGER.debug("auth null");
            return Response.status(Response.Status.UNAUTHORIZED).build(); // 401 Unauthorized
        }
        LOGGER.debug("auth passed");

        // Acquire an EntityManager (creating a new persistence context).
        EntityManager em = PersistenceManager.instance().createEntityManager();

        try {
            // Start a new transaction.
            em.getTransaction().begin();

            User user = em.createQuery("FROM User u WHERE u.cookie = :cookie", User.class).setParameter("cookie", auth.getValue()).getSingleResult();

            if (user == null) {
                LOGGER.debug("user null");
                shutdownEM(em);
                return Response.status(Response.Status.UNAUTHORIZED).build(); // 401 Unauthorized
            }

            Booking booking = em.createQuery("FROM Booking b WHERE b.id = :id", Booking.class).setParameter("id", id).getSingleResult();

            if (booking == null) {
                LOGGER.debug("booking null");
                shutdownEM(em);
                return Response.status(Response.Status.NOT_FOUND).build(); // 404 Not Found
            }

            if (!booking.getUser().equals(user)) {
                LOGGER.debug("user not authorized");
                shutdownEM(em);
                return Response.status(Response.Status.FORBIDDEN).build(); // 403 Forbidden
            }

            bookingDto = BookingMapper.toDto(booking);

            // Commit the transaction.
            em.getTransaction().commit();
        } finally {
            // Close EntityManager to free up resources.
            em.close();
        }

        LOGGER.debug("return booking");
        return Response.ok(bookingDto).build();
    }

    @GET
    @Path("/bookings")
    @Produces({MediaType.APPLICATION_JSON, SerializationMessageBodyReaderAndWriter.APPLICATION_JAVA_SERIALIZED_OBJECT})
    public Response getBookings(@CookieParam("auth") Cookie auth) {
        LOGGER.debug("Start GET /bookings/");

        List<BookingDTO> bookingDtos = new ArrayList<>();

        // Test to see if User is logged in.
        if (auth == null) {
            LOGGER.debug("auth null");
            return Response.status(Response.Status.UNAUTHORIZED).build(); // 401 Unauthorized
        }
        LOGGER.debug("auth passed");

        // Acquire an EntityManager (creating a new persistence context).
        EntityManager em = PersistenceManager.instance().createEntityManager();

        try {
            em.getTransaction().begin();
            User user = em.createQuery("FROM User u WHERE u.cookie = :cookie", User.class).setParameter("cookie", auth.getValue()).getSingleResult();

            if (user == null) {
                LOGGER.debug("user null");
                shutdownEM(em);
                return Response.status(Response.Status.UNAUTHORIZED).build(); // 401 Unauthorized
            }

            List<Booking> bookings = em.createQuery("FROM Booking b WHERE b.user.id = :userId", Booking.class).setParameter("userId", user.getId()).getResultList();
            em.getTransaction().commit();
            if (bookings == null || bookings.isEmpty()) {
                LOGGER.debug("bookings null");
                shutdownEM(em);
                return Response.ok(bookingDtos).build(); // Return empty list
            }

            for (Booking booking : bookings) {
                if (!booking.getUser().equals(user)) {
                    LOGGER.debug("user not authorized");
                    shutdownEM(em);
                    return Response.status(Response.Status.FORBIDDEN).build(); // 403 Forbidden
                }

                BookingDTO bookingDto = BookingMapper.toDto(booking);
                bookingDtos.add(bookingDto);
            }
        } finally {
            // Close EntityManager to free up resources.
            em.close();
        }

        LOGGER.debug("return bookings");
        return Response.ok(bookingDtos).build();
    }

    @POST
    @Path("/subscribe/concertInfo")
    @Produces({MediaType.APPLICATION_JSON, SerializationMessageBodyReaderAndWriter.APPLICATION_JAVA_SERIALIZED_OBJECT})
    public void subscribe(@Suspended AsyncResponse response, ConcertInfoSubscriptionDTO subInfo, @CookieParam("auth") Cookie auth) {

        // Test to see if User is logged in.
        if (auth == null) {
            LOGGER.debug("auth null");
            response.resume(Response.status(401).build());
            return;
        }
        LOGGER.debug("auth passed");

        EntityManager em = PersistenceManager.instance().createEntityManager();
        LocalDateTime date = subInfo.getDate();

        try {
            User user = em.createQuery("FROM User u WHERE u.cookie = :cookie", User.class).setParameter("cookie", auth.getValue()).getSingleResult();

            // Authenticate user
            if (user == null) {
                LOGGER.debug("user null");
                response.resume(Response.status(Response.Status.UNAUTHORIZED).build()); // UNAUTHORIZED
                return;
            }

            // Check if concert exist with id
            Concert concert = em.find(Concert.class, subInfo.getConcertId());
            if (concert == null) {
                LOGGER.debug("concert null");
                response.resume(Response.status(Response.Status.BAD_REQUEST).build()); // 400 Bad Request
                return;
            }

            // Check if concert date is correct
            if (concert.getDates().stream().noneMatch(d -> d.equals(date))) {
                LOGGER.debug("date not in concert");
                response.resume(Response.status(Response.Status.BAD_REQUEST).build()); // 400 Bad Request
                return;
            }

            ConcertInfoSubscription concertInfoSubscription = ConcertInfoSubscriptionMapper.toDomainModel(subInfo, user);

            // Persist the concertInfoSubscription to the database.
            em.getTransaction().begin();
            em.persist(concertInfoSubscription);
            em.getTransaction().commit();
            LOGGER.debug("Add concertInfoSubscription to database =" + concertInfoSubscription.getId());

            // Add the AsyncResponse to the map.
            asyncResponses.put(concertInfoSubscription.getId(), response);
        } finally {
            em.close();
        }
        LOGGER.debug("asyncResponses size: " + asyncResponses.size());

        checkSubscriptions(date);

        LOGGER.debug("=== End POST /subscribe/concertInfo ===");
    }

    private void shutdownEM(EntityManager em) {
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
        em.close();
    }

    private void checkSubscriptions(LocalDateTime date) {
        LOGGER.debug("checkSubscriptions");

        List<Seat> seats;
        List<ConcertInfoSubscription> subscriptions;

        EntityManager em = PersistenceManager.instance().createEntityManager();

        try {
            subscriptions = em.createQuery("FROM ConcertInfoSubscription WHERE date = :date", ConcertInfoSubscription.class).setParameter("date", date).getResultList();

            if (subscriptions == null) {
                return;
            }

            // Get amount of booked seats.
            seats = em.createQuery("FROM Seat WHERE date = :date", Seat.class).setParameter("date", date).getResultList();


            if (seats == null) {
                return;
            }

            LOGGER.debug("subs size before: " + subscriptions.size());
            List<Seat> bookedSeats = seats.stream().filter(s -> s.getIsBooked()).collect(Collectors.toList());
            LOGGER.debug("booked seats: " + bookedSeats.size() + " seats: " + seats.size());

            // Check if any subscriptions can be fulfilled.
            synchronized (subscriptions) {
                for (ConcertInfoSubscription subscription : subscriptions) {
                    // Check if subscription is for the correct date.
                    if (!subscription.getDate().equals(date)) {
                        continue;
                    }

                    AsyncResponse response = asyncResponses.get(subscription.getId());

                    int seatsNeeded = (seats.size() * subscription.getPercentageBooked()) / 100;

                    if (bookedSeats.size() < seatsNeeded) {
                        continue; // Not enough seats booked.
                    }

                    // Send response to client.
                    if (response != null) {
                        int numSeatsRemaining = seats.size() - bookedSeats.size();
                        ConcertInfoNotificationDTO concertInfoNotificationDto = new ConcertInfoNotificationDTO(numSeatsRemaining);
                        response.resume(concertInfoNotificationDto); // Send response to client.

                        asyncResponses.remove(subscription.getId());
                    }

                    // Remove subscription from database.
                    try {
                        em.getTransaction().begin();
                        em.remove(subscription);
                        em.getTransaction().commit();
                    } catch (Exception e) {
                        LOGGER.debug("Exception: " + e.getMessage());
                    }
                }
            }
        } finally {
            shutdownEM(em);
        }

        LOGGER.debug("subs size after: " + subscriptions.size());
    }
}