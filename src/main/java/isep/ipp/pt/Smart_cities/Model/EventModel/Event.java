package isep.ipp.pt.Smart_cities.Model.EventModel;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import isep.ipp.pt.Smart_cities.Model.UserModel.User;

@Getter
@Setter
@ToString
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    private String title;

    @NotBlank(message = "Location is required")
    @Size(max = 255, message = "Location cannot exceed 255 characters")
    private String location;

    @FutureOrPresent(message = "Start date must not be in the past")
    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @FutureOrPresent(message = "End date must be in the future")
    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @Pattern(regexp = "^(Art|Sports|Volunteering|Social|Educational|Recreational|Political)$", message = "Invalid category, please choose one from: Art, Sports, Volunteering, Social, Educational, Recreational or Political")
    private String category;

    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    private String imagePath;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User creator;

    public Event() {}

    public Event(String title, String location, LocalDate startDate, LocalDate endDate, String description, User creator) {
        this.title = title;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.creator = creator;
    }
    /* 
    public void addCategory(String category) {
        categories.add(category);
    }
    
    public void removeCategory(String category) {
        categories.remove(category);
    }*/
}

