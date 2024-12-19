package isep.ipp.pt.Smart_cities.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Badge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    private LocalDateTime completionDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BadgeCategory category;

    @ManyToOne(optional = false)
    private User user;
}
public enum BadgeCategory {
    BEGINNER_MILESTONES,
    SOCIAL_INTERACTION,
    FREQUENCY_LOYALTY
}

