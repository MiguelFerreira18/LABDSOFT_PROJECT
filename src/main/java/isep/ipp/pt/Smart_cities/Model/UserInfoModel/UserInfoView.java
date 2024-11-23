package isep.ipp.pt.Smart_cities.Model.UserInfoModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

@Data
public class UserInfoView {

    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date birthDate;
    private String gender;
    private String address;
    private String city;
    private String country;

    public UserInfoView(String name,Date birthDate, String gender, String address, String city, String country) {
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
        this.city = city;
        this.country = country;
    }

}

