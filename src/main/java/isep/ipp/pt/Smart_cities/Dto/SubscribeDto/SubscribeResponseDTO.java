package isep.ipp.pt.Smart_cities.Dto.SubscribeDto;

import isep.ipp.pt.Smart_cities.Model.Event;
import isep.ipp.pt.Smart_cities.Model.SubscriptionStatus;

public class SubscribeResponseDTO {
    private Long id;
    private Event events;
    private int code;
    private SubscriptionStatus status;

    public SubscribeResponseDTO() {
    }

    public SubscribeResponseDTO(Long id, Event event, int code, SubscriptionStatus status) {
        this.id = id;
        this.events = event;
        this.code = code;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Event getEvents() {
        return events;
    }

    public void setEvents(Event events) {
        this.events = events;
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
}
