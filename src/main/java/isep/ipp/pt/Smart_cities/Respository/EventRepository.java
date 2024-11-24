package isep.ipp.pt.Smart_cities.Respository;

import isep.ipp.pt.Smart_cities.Model.EventModel.Event;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends CrudRepository<Event, String> {

    //@Query("SELECT e FROM Event e WHERE e.creator.email = ?1")
    //public List<Event> findByCreatorEmail(String email);

    @Query("SELECT e FROM Event e WHERE e.title = ?1")
    public Optional<Event> findByTitle(String title);

    @Query("SELECT e FROM Event e WHERE e.startDate >= CURRENT_DATE")
    public List<Event> findUpcomingEvents();

    @Query("SELECT e FROM Event e WHERE e.category = ?1")
    public List<Event> findByCategory(String category);

    // Spring Data JPA provides findAll() by default, so no need for a custom query.
    public List<Event> findAll();

    @Query("SELECT e FROM Event e WHERE e.promotedUntil > :now")
    List<Event> findPromotedEvents(LocalDateTime now);

    @Query("SELECT e FROM Event e WHERE e.promotedUntil IS NULL OR e.promotedUntil <= :now")
    List<Event> findNonPromotedEvents(LocalDateTime now);

    List<Event> findByCreatorEmail(String email);
}
