package isep.ipp.pt.Smart_cities.Respository;

import isep.ipp.pt.Smart_cities.Model.Subscribe;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SubscribeRepo extends CrudRepository<Subscribe, Long> {


    @Query("SELECT s FROM Subscribe s WHERE s.user.id = ?1")
    Iterable<Subscribe> findAllSubscribedEventsFromUser(String id);

    @Query("SELECT s FROM Subscribe s WHERE s.event.id = ?1 AND s.user.id = ?2")
    Optional<Subscribe> findByEventIdAndUserId(long eventId, String userId);
}