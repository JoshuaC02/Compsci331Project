## All members of Group 13:

- @JoshuaC02 (Joshua Coleman, jcol984)
- @andrewmozolev (Andrew Mozolev, amoz342)
- @jluo896 (Jesse Luo, jluo896)

## Write up - Domain Model

**Joshua Coleman**: My main task for the Domain model was checking for any bugs. We had a lot of trouble at the outset with association tables not generating properly. We found the solution to this to be incorrect/missing annotations, which took about a day to resolve. The Domain Model was completed in 3 days, which was longer than we anticipated but we allowed extra time in our initial planning phase for errors so we still had more than enough allocated time to complete the rest of our project.

**Andrew Mozolev**: For the Domain model, I created domain model classes for Concert, Performer, and Seat. Added some fixes to User and Booking domain models after reviewing the code. Also, we had issues with proper persistence annotations for the domain models because we had to populate the database with data in a specific way from `db-init.sql`, which we managed to fix with Joshua. To sum up, we faced many issues and doubts about implementation, which we discussed with Joshua and managed to resolve them.

**Jesse Luo**: My contribution to the Domain model was the initial implementation of the User Class. Creating the variables and their respective @Column name based on the db-init and its OnetoMany relationship to bookings.

## Write up - ConcertResource

**Joshua Coleman**: Completing the ConcertResource was not a straight forward process in the slighest. There were small changes that needed to be made to the DTO classes to get them working (serializing, annotations) but without them, the entire application fell apart. Andrew and I worked on the main endpoints and left Jesse to focus on the subscription endpoints.

**Andrew Mozolev**: For the ConcertResource I implemented GET `/concerts/{id}`, GET `/concerts`, GET `/seats`, POST `/booking`, GET `/bookings` endpoints. Joshua and I tested our endpoints, discussed implementations and resolved bugs and issues. Apart from that, I added Jackson serialization annotations to DTO classes. For mapping between DTO and domain model classes, I added BookingMapper, ConcertMapper, PerformerMapper and SeatMapper. Jesse implemented subscription endpoints, but there was a performance issue due to the while true loop. So, I refactored it by saving subscriptions to DB and async responses to the map in ConcertResouce class.

**Jesse Luo**: The Subscription functionality was my task for ConcertResources, I started by making a subs list like in the lectures however the list kept on being recreated when another method is called. I couldn't figure out a way to solve this so I tried using a ThreadPool instead and a while loop to keep it active. The first version where all test case passes, would open Enimity manager numerous times to check the number of booked seats which was expensive. Then I thought that creating a boolean variable which the while loop checks before opening Entity Manager then sets false and the boolean will only be set true when Booking was called. However, we felt like there are better ways to implement Subscription.

## Strategy used to minimise concurrency errors in program execution

We implemented a version variable with the annotation `@Version` in each object that kept track of how many times the object was updated with `LockModeType.OPTIMISTIC_FORCE_INCREMENT`. If the new update's version number was not greater than the current version number, the update would be rejected and it would throw `OptimisticLockException` error.

## How the domain model is organised

We used `cascade = CascadeType.PERSIST` for `performers` in Concert to save performance to the DB when we create new concert and `fetch = FetchType.EAGER` for `performers` in Concert to load performers when we load concert from the DB.
We used `cascade = CascadeType.ALL` and `orphanRemoval = true` for `bookings` in User to save and delete bookings when we create new user and delete user from the DB.

DB schema:
![DB Schema](https://user-images.githubusercontent.com/15260025/236654931-3c342cb0-92ef-4275-9376-cf8ff9da41e5.png)