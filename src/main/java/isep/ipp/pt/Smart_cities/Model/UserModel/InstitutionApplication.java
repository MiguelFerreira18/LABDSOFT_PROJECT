package isep.ipp.pt.Smart_cities.Model.UserModel;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;



@Entity
public class InstitutionApplication {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)  // Automatically generate an ID if it's not provided
    private String id;

    @isep.ipp.pt.Smart_cities.Model.UserModel.NotBlank
    private String institutionName;
    @isep.ipp.pt.Smart_cities.Model.UserModel.NotBlank
    private String userId;

    private String status;

    private String description;

    private String contactInformation;

    // Getters and Setters
    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContactInformation() {
        return contactInformation;
    }

    public void setContactInformation(String contactInformation) {
        this.contactInformation = contactInformation;
    }
}