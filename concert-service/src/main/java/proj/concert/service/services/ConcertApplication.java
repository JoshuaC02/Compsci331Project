package proj.concert.service.services;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import proj.concert.service.util.ConcertUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * This is the Application class for the concert service. This class is complete -- you should not need to modify it
 * (but marks will not be deducted if you decide you need to do so).
 */
@ApplicationPath("/services")
public class ConcertApplication extends Application {

    private Set<Object> singletons = new HashSet<>();
    private Set<Class<?>> classes = new HashSet<>();

    public ConcertApplication() {
        classes.add(TestResource.class);
        classes.add(ConcertResource.class);
        singletons.add(PersistenceManager.instance());
        ConcertUtils.initConcerts();
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }

    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }

}
