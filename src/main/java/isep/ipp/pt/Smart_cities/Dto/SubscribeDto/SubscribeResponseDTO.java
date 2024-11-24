package isep.ipp.pt.Smart_cities.Dto.SubscribeDto;

import isep.ipp.pt.Smart_cities.Model.EventModel.Event;
import isep.ipp.pt.Smart_cities.Model.SubscriptionStatus;

public class SubscribeResponseDTO {
    private Long id;
    private Event event;
    private int code;
    private SubscriptionStatus status;
    private Double rate;

    public SubscribeResponseDTO() {
    }

    public SubscribeResponseDTO(Long id, Event event, int code, SubscriptionStatus status, double rate) {
        this.id = id;
        this.event = event;
        this.code = code;
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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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
