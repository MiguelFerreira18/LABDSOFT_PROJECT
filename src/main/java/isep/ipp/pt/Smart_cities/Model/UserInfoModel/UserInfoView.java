package isep.ipp.pt.Smart_cities.Model.UserInfoModel;

import lombok.Data;
import java.util.Date;

@Data
public class UserInfoView {
    private Date birthDate;
    private String gender;
    private String address;
    private String city;
    private String country;

    public UserInfoView(Date birthDate, String gender, String address, String city, String country) {
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
        this.city = city;
        this.country = country;
    }

}

