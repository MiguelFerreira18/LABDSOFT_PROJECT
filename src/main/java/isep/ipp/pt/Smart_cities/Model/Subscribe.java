package isep.ipp.pt.Smart_cities.Model;

import isep.ipp.pt.Smart_cities.Model.UserModel.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@Entity
public class Subscribe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Event event;

    private int code;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus subscriptionStatus;

    public Subscribe() {
    }

    public Subscribe(User user, Event event) {
        this.user = user;
        this.event = event;
    }

    public Subscribe(User user, int code) {
        this.user = user;
        this.code = code;
    }

    public Subscribe(Long id, User user, int code, SubscriptionStatus subscriptionStatus) {
        this.id = id;
        this.user = user;
        this.code = code;
        this.subscriptionStatus = subscriptionStatus;
    }

    public Subscribe(Long id, User user, Event event, int code, SubscriptionStatus subscriptionStatus) {
        this.id = id;
        this.user = user;
        this.event = event;
        this.code = code;
        this.subscriptionStatus = subscriptionStatus;
    }



}