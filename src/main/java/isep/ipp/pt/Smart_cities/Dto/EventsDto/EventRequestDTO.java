package isep.ipp.pt.Smart_cities.Dto.EventsDto;

import isep.ipp.pt.Smart_cities.Model.EventModel.Event;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;


@Getter
@Builder
public class EventRequestDTO {

    private String title;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private String descrption;
    private String category;
    private String creatorID;

    public EventRequestDTO() {
    }

    public EventRequestDTO(String title, String location, LocalDate startDate, LocalDate endDate, String descrption, String category, String creatorID) {
        this.title = title;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.descrption = descrption;
        this.category = category;
        this.creatorID = creatorID;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EventRequestDTO{");
        sb.append("title='").append(title).append('\'');
        sb.append(", location='").append(location).append('\'');
        sb.append(", startDate=").append(startDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", descrption='").append(descrption).append('\'');
        sb.append(", category='").append(category).append('\'');
        sb.append(", creatorID='").append(creatorID).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
