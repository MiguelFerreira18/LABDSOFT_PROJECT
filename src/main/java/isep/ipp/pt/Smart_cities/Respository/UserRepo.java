package isep.ipp.pt.Smart_cities.Respository;

import isep.ipp.pt.Smart_cities.Model.UserModel.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepo extends CrudRepository<User, String> {

    @Query("SELECT u FROM User u WHERE u.email = ?1")
    public Optional<User> findByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.lastLoginAt = ?2 WHERE u.email = ?1")
    public void updateUserLastLogin(String userEmail, LocalDateTime date);
}
