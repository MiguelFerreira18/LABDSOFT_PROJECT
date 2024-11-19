package isep.ipp.pt.Smart_cities.Model;

import isep.ipp.pt.Smart_cities.Dto.RewardsDto.RewardResponseDTO;
import isep.ipp.pt.Smart_cities.Model.EventModel.Event;
import isep.ipp.pt.Smart_cities.Model.UserModel.User;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@Entity
public class Rewards {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int points;

    private int dailyStreakDays;

    @ManyToOne
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id",nullable = true)
    private Event event;

    private LocalDateTime lastLoginAt = LocalDateTime.now().minusDays(5);

    public Rewards() {
    }

    public Rewards(int id, int points, User user, Event event) {
        this.id = id;
        this.points = points;
        this.user = user;
        this.event = event;
    }

    public Rewards(int id, int points, int dailyStreakDays, User user, Event event) {
        this.id = id;
        this.points = points;
        this.dailyStreakDays = dailyStreakDays;
        this.user = user;
        this.event = event;
    }

    public Rewards(int id, int points, int dailyStreakDays, User user, Event event, LocalDateTime lastLoginAt) {
        this.id = id;
        this.points = points;
        this.dailyStreakDays = dailyStreakDays;
        this.user = user;
        this.event = event;
        this.lastLoginAt = lastLoginAt;
    }

    public RewardResponseDTO toDTO(int pointsToAdd) {
        return RewardResponseDTO.builder()
                .points(this.points)
                .dailyStreakDays(this.dailyStreakDays)
                .pointsEarned(pointsToAdd)
                .build();
    }

    public boolean hasLoggedInToday(LocalDateTime userLastLoginAt) {

        var dia = this.lastLoginAt;
        if(userLastLoginAt.getDayOfMonth() == LocalDateTime.now().getDayOfMonth()){
            if(this.lastLoginAt.getDayOfMonth() == LocalDateTime.now().getDayOfMonth()){
                return true;
            }
        }

        return false;
    }
}
