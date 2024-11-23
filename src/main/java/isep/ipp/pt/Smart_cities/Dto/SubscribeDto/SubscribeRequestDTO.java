package isep.ipp.pt.Smart_cities.Dto.SubscribeDto;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SubscribeRequestDTO {
    @NotNull
    private String uuid;
    @NotNull
    private String eventId;
}
