package isep.ipp.pt.Smart_cities.Respository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import isep.ipp.pt.Smart_cities.Model.UserInfoModel.UserInfo;

import java.util.Optional;

@Repository
public interface UserInfoRepo extends CrudRepository<UserInfo, String> {

    @Query("SELECT u FROM UserInfo u WHERE u.userId = ?1")
    public Optional<UserInfo> findByUserId(String id);
}
