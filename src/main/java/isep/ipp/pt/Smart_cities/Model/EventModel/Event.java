package isep.ipp.pt.Smart_cities.Model.EventModel;

import isep.ipp.pt.Smart_cities.Dto.EventsDto.EventRequestDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import isep.ipp.pt.Smart_cities.Model.UserModel.User;

@Builder
@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
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

    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;

    @NotNull(message = "End date is required")
    private LocalDateTime endDate;

    @Pattern(regexp = "^(Art|Sports|Volunteering|Social|Educational|Recreational|Political)$", message = "Invalid category, please choose one from: Art, Sports, Volunteering, Social, Educational, Recreational or Political")
    private String category;

    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @Column(name = "event_limit")
    private int limit;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User creator;

    private LocalDateTime promotedUntil;

    private float latitude;

    private float longitude;

    private float rating;

    public Event(String title, String location, LocalDateTime startDate, LocalDateTime endDate, String description, User creator) {
        this.title = title;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.creator = creator;
        this.limit = 0;
    }

    public Event(String title, String location, LocalDateTime startDate, LocalDateTime endDate, String description, User creator, LocalDateTime promotedUntil) {
        this.title = title;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.creator = creator;
        this.promotedUntil = null;
        this.limit = 0;
    }


    public Event(String id, String title, String location, LocalDateTime startDate, LocalDateTime endDate, String category, String description, List<Image> images, User creator, LocalDateTime promotedUntil) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.category = category;
        this.description = description;
        this.images = images; 
        this.creator = creator;
        this.promotedUntil = promotedUntil;
        this.limit = 0;
    }

    public Event(String id, String title, String location, LocalDateTime startDate, LocalDateTime endDate, String category, String description,int limit, List<Image> images, User creator, LocalDateTime promotedUntil, float latitude, float longitude) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.category = category;
        this.description = description;
        this.limit = limit;
        this.images = images;
        this.creator = creator;
        this.promotedUntil = promotedUntil;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Boolean isInCurrentMonth() {
        LocalDate now = LocalDate.now();
        return endDate.getMonthValue() == now.getMonthValue() && endDate.getYear() == now.getYear();
    }

    public boolean isPromoted() {
        return promotedUntil != null && promotedUntil.isAfter(LocalDateTime.now());
    }

    public EventRequestDTO toEventRequestDTO() {
        return EventRequestDTO.builder()
                .title(title)
                .location(location)
                .startDate(startDate.toLocalDate())
                .endDate(endDate.toLocalDate())
                .description(description)
                .category(category)
                .creatorID(creator.getId())
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }


    public void addImage(Image image) {
        image.setEvent(this);
        this.images.add(image);
    }

    public List<Image> getImages() {
        if (images == null) {
            images = new ArrayList<>();
        }
        return images;
    }
}