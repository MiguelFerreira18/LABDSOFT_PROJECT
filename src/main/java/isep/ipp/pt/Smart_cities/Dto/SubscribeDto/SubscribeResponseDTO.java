package isep.ipp.pt.Smart_cities.Dto.SubscribeDto;

import isep.ipp.pt.Smart_cities.Model.Encrypted.EncryptedEvent;

import java.util.List;

public class SubscribeResponseDTO {
    private String uuid;
    private EncryptedEvent events;
    private String code;
    private String status;

    public SubscribeResponseDTO() {
    }

    public SubscribeResponseDTO(String uuid, EncryptedEvent event, String code, String status) {
        this.uuid = uuid;
        this.events = event;
        this.code = code;
        this.status = status;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public EncryptedEvent getEvents() {
        return events;
    }

    public void setEvent(EncryptedEvent event) {
        this.events = event;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
