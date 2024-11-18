package isep.ipp.pt.Smart_cities.Dto.RewardsDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RewardResponseDTO {

    private Long id;
    private int points;


    public RewardResponseDTO() {
    }
    public RewardResponseDTO(Long id, int points) {
        this.id = id;
        this.points = points;
    }
}
