package isep.ipp.pt.Smart_cities.Respository;

import isep.ipp.pt.Smart_cities.Model.Subscribe;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface SubscribeRepo extends CrudRepository<Subscribe, Long> {


    @Query("SELECT s FROM Subscribe s WHERE s.user.id = ?1")
    Iterable<Subscribe> findAllSubscribedEventsFromUser(String id);
}
