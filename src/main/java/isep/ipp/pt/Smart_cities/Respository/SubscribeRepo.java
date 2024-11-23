package isep.ipp.pt.Smart_cities.Respository;

import isep.ipp.pt.Smart_cities.Model.Subscribe;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscribeRepo extends CrudRepository<Subscribe, Long> {


    @Query("SELECT s FROM Subscribe s WHERE s.user.email = ?1")
    Iterable<Subscribe> findAllSubscribedEventsFromUser(String email);

    @Query("SELECT s FROM Subscribe s WHERE s.event.id = ?1 AND s.user.id = ?2")
    Optional<Subscribe> findByEventIdAndUserId(String eventId, String userId);

    @Query("SELECT s FROM Subscribe s WHERE s.event.id = ?1")
    Iterable<Subscribe> findAllByEventId(String eventId);

    @Query("SELECT s FROM Subscribe s WHERE s.user.id = ?1")
    Iterable<Subscribe> findAllByUserId(String userId);
}
