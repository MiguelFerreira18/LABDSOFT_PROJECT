package isep.ipp.pt.Smart_cities.Dto.SubscribeDto;

import isep.ipp.pt.Smart_cities.Model.EventModel.Event;
import isep.ipp.pt.Smart_cities.Model.SubscriptionStatus;

public class SubscribeResponseDTO {
    private Long id;
    private Event event;
    private String QRData;
    private SubscriptionStatus status;
    private Double rate;

    public SubscribeResponseDTO() {
    }

    public SubscribeResponseDTO(Long id, Event event, String QRData, SubscriptionStatus status) {
        this.id = id;
        this.event = event;
        this.QRData = QRData;
        this.status = status;
        this.rate = rate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getQRData() {
        return QRData;
    }

    public void setQRData(String QRData) {
        this.QRData = QRData;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }
}
