package isep.ipp.pt.Smart_cities.Respository;

import isep.ipp.pt.Smart_cities.Model.UserModel.Institution;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface InstitutionRepo extends CrudRepository<Institution, String> {

    @Query("SELECT i FROM Institution i WHERE i.email = ?1")
    public Optional<Institution> findByEmail(String email);
}
