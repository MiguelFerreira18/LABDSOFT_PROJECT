package isep.ipp.pt.Smart_cities.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class RateDTO {
    @NotNull
    private String uuid;
    @NotNull
    private String eventId;
    @NotNull
    private String rating;
}
