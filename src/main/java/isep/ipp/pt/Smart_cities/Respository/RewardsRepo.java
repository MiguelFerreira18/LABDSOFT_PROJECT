package isep.ipp.pt.Smart_cities.Respository;

import isep.ipp.pt.Smart_cities.Model.Rewards;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RewardsRepo extends CrudRepository<Rewards,Long> {
    @Query("SELECT r FROM Rewards r WHERE r.user.id = ?1")
    Iterable<Rewards> findAllByUserId(String userId);

    @Query("SELECT r FROM Rewards r where r.user.id = ?1 and r.event.id IS NULL")
    Optional<Rewards> findLoginPointsByUser(String userId);
}
