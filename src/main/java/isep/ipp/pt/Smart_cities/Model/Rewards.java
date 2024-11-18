package isep.ipp.pt.Smart_cities.Model;

import isep.ipp.pt.Smart_cities.Dto.RewardsDto.RewardResponseDTO;
import isep.ipp.pt.Smart_cities.Model.EventModel.Event;
import isep.ipp.pt.Smart_cities.Model.UserModel.User;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@Entity
public class Rewards {
    @Id
    @GeneratedValue
    private Long id;
    private int points;
    @ManyToOne
    private User user;
    @ManyToOne
    @JoinColumn(name = "event_id",nullable = true)
    private Event event;

    public Rewards() {
    }

    public Rewards(Long id, int points, User user, Event event) {
        this.id = id;
        this.points = points;
        this.user = user;
        this.event = event;
    }

    public Rewards(int points, User user, Event event) {
        this.points = points;
        this.user = user;
        this.event = event;
    }

    public RewardResponseDTO toDTO() {
        return RewardResponseDTO.builder()
                .id(this.id)
                .points(this.points)
                .build();
    }
}
