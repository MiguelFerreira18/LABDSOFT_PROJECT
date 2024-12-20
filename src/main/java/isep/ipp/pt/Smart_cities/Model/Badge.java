package isep.ipp.pt.Smart_cities.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import isep.ipp.pt.Smart_cities.Model.UserModel.User;

@Getter
@Setter
@Entity
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BadgeCategory category;  // This defines the category of the badge (e.g., social, frequency, etc.)

    private LocalDateTime completionDate;  // Date when the badge was awarded

    private String iconPath;  // Path to the badge icon

    @ManyToOne(optional = false)
    private User user;  // The user who earned the badge

    @ManyToOne(optional = false)
    private Milestone milestone;  // The milestone that was completed to earn the badge

    // Method to set the icon path
    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    // Method to set the completion date
    public void setCompletionDate() {
        this.completionDate = LocalDateTime.now();  // Set the current date/time when the badge is awarded
    }
}