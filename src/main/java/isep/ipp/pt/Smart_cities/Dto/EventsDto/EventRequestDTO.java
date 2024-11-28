package isep.ipp.pt.Smart_cities.Dto.EventsDto;

import lombok.*;

import java.time.LocalDate;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestDTO {

    private String title;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private int limit;
    private String description;
    private String category;
    private String creatorID;
    private float latitude;
    private float longitude;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EventRequestDTO{");
        sb.append("title='").append(title).append('\'');
        sb.append(", location='").append(location).append('\'');
        sb.append(", startDate=").append(startDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", descrption='").append(description).append('\'');
        sb.append(", limit=").append(limit);
        sb.append(", category='").append(category).append('\'');
        sb.append(", creatorID='").append(creatorID).append('\'');
        sb.append(", latitude=").append(latitude);
        sb.append(", longitude=").append(longitude);
        sb.append('}');
        return sb.toString();
    }
}
