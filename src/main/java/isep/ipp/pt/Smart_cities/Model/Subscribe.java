package isep.ipp.pt.Smart_cities.Model;

import isep.ipp.pt.Smart_cities.Dto.SubscribeDto.SubscribeResponseDTO;
import isep.ipp.pt.Smart_cities.Model.EventModel.Event;
import isep.ipp.pt.Smart_cities.Model.UserModel.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@Builder
@ToString
@Entity
public class Subscribe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Event event;

    private int code;

    private boolean isNotified;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus subscriptionStatus;

    public Subscribe() {
        this.isNotified = false;
        this.subscriptionStatus = SubscriptionStatus.SUBSCRIBED;
    }

    public Subscribe(User user, Event event) {
        this.user = user;
        this.event = event;
        this.isNotified = false;
    }

    public Subscribe(User user, int code) {
        this.user = user;
        this.code = code;
        this.isNotified = false;
    }

    public Subscribe(Long id, User user, int code, SubscriptionStatus subscriptionStatus) {
        this.id = id;
        this.user = user;
        this.code = code;
        this.subscriptionStatus = subscriptionStatus;
        this.isNotified = false;
    }

    public Subscribe(Long id, User user, Event event, int code, SubscriptionStatus subscriptionStatus,boolean isNotified) {
        this.id = id;
        this.user = user;
        this.event = event;
        this.code = code;
        this.subscriptionStatus = subscriptionStatus;
        this.isNotified = isNotified;
    }

    public Subscribe(Long id, User user, Event event, int code, boolean isNotified, SubscriptionStatus subscriptionStatus) {
        this.id = id;
        this.user = user;
        this.event = event;
        this.code = code;
        this.isNotified = isNotified;
        this.subscriptionStatus = subscriptionStatus;
    }

    public SubscribeResponseDTO toDTO(){
        return new SubscribeResponseDTO(this.id, this.event, this.code, this.subscriptionStatus);
    }



}
