package isep.ipp.pt.Smart_cities.Respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import isep.ipp.pt.Smart_cities.Model.EventModel.Image;

@Repository
public interface ImageRepo extends JpaRepository<Image, Long> {

    @Query("SELECT i FROM Image i WHERE i.event.id = :eventId")
    List<Image> findByEventId(String eventId);
}