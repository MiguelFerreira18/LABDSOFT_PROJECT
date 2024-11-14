package isep.ipp.pt.Smart_cities.Model.UserInfoModel;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id")
    private String userId;

    @Column
    private Date birthDate;

    @Column
    private String gender;

    @Column
    private String address;

    @Column
    private String city;

    @Column
    private String country;


    public UserInfo (String userId, Date birthDate, String gender, String address, String city, String country) {
        this.userId = userId;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
        this.city = city;
        this.country = country;
    }
}
