package isep.ipp.pt.Smart_cities.Model.UserModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class UserView {
    String id;
    String name;
    Set<Role> authorities;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    Date birthDate;
    String gender;
    String address;
    String city;
    String country;

    public UserView(String id, String name, Set<Role> authorities) {
        this.id = id;
        this.name = name;
        this.authorities = authorities;
    }

    public UserView(String id, String name, Set<Role> authorities, Date birth, String gender, String address, String city, String country) {
        this.id = id;
        this.name = name;
        this.authorities = authorities;
        this.birthDate = birth;
        this.gender = gender;
        this.address = address;
        this.city = city;
        this.country = country;
    }

}
