package isep.ipp.pt.Smart_cities.Model;

import isep.ipp.pt.Smart_cities.Dto.SubscribeDto.SubscribeResponseDTO;
import isep.ipp.pt.Smart_cities.Model.EventModel.Event;
import isep.ipp.pt.Smart_cities.Model.UserModel.User;
import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
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

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Event event;

    private String QRData;

    private boolean isNotified;

    private double rate;

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

    public Subscribe(User user, String QRData) {
        this.user = user;
        this.QRData = QRData;
        this.isNotified = false;
    }

    public Subscribe(Long id, User user, String QRData, SubscriptionStatus subscriptionStatus) {
        this.id = id;
        this.user = user;
        this.QRData = QRData;
        this.subscriptionStatus = subscriptionStatus;
        this.isNotified = false;
    }

    public Subscribe(Long id, User user, Event event, String QRData, SubscriptionStatus subscriptionStatus,boolean isNotified) {
        this.id = id;
        this.user = user;
        this.event = event;
        this.QRData = QRData;
        this.subscriptionStatus = subscriptionStatus;
        this.isNotified = isNotified;
    }

    public Subscribe(Long id, User user, Event event, String QRData, boolean isNotified, SubscriptionStatus subscriptionStatus) {
        this.id = id;
        this.user = user;
        this.event = event;
        this.QRData = QRData;
        this.isNotified = isNotified;
        this.subscriptionStatus = subscriptionStatus;
    }

    public Subscribe(Long id, User user, Event event, String QRData, boolean isNotified, SubscriptionStatus subscriptionStatus, double rate) {
        this.id = id;
        this.user = user;
        this.event = event;
        this.QRData = QRData;
        this.isNotified = isNotified;
        this.subscriptionStatus = subscriptionStatus;
        this.rate = rate;
    }

    public SubscribeResponseDTO toDTO(){
        return new SubscribeResponseDTO(this.id, this.event, this.QRData, this.subscriptionStatus, this.rate);
    }
}