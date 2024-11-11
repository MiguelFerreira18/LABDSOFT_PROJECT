package isep.ipp.pt.Smart_cities.Dto.SubscribeDto;

import isep.ipp.pt.Smart_cities.Util.EncryptionUtil;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.UUID;

@Getter
@ToString
public class SubscribeRequestDTO {
    @NotNull
    private String uuid;
    @NotNull
    private String eventId;

    public String decryptUuid(EncryptionUtil util) {
        return util.decrypt(uuid).orElse(null);
    }

    public String decryptEventId(EncryptionUtil util) {
        return util.decrypt(eventId).orElse(null);
    }
}
