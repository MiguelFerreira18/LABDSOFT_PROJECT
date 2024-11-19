package isep.ipp.pt.Smart_cities.Dto.RewardsDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RewardResponseDTO {

    private int points;

    private int dailyStreakDays;

    private int pointsEarned;

    public RewardResponseDTO() {
    }
    public RewardResponseDTO(int points) {
        this.points = points;
    }

    public RewardResponseDTO(int points, int dailyStreakDays, int pointsEarned) {
        this.points = points;
        this.dailyStreakDays = dailyStreakDays;
        this.pointsEarned = pointsEarned;
    }
}
