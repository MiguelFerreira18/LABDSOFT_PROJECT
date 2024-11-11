package isep.ipp.pt.Smart_cities.Mapper.SubscribeMapper;

import isep.ipp.pt.Smart_cities.Dto.SubscribeDto.SubscribeResponseDTO;
import isep.ipp.pt.Smart_cities.Model.Encrypted.EncryptedEvent;
import isep.ipp.pt.Smart_cities.Model.Encrypted.EncryptedSubscribe;
import isep.ipp.pt.Smart_cities.Model.Subscribe;
import isep.ipp.pt.Smart_cities.Util.EncryptionUtil;
import org.springframework.stereotype.Component;

@Component
public class SubscribeMapperImpl implements SubscribeMapper {

    @Override
    public SubscribeResponseDTO toSubscribeResponseDTO(Subscribe subscribe, EncryptionUtil util) {
        if (subscribe == null) {
            return null;
        }

        EncryptedSubscribe encryptedSubscribe = subscribe.returnEncryptedSubscribe(util);

        String uuid = encryptedSubscribe.getUuid();
        EncryptedEvent event = encryptedSubscribe.getEvent();
        String code = encryptedSubscribe.getCode();
        String status = encryptedSubscribe.getStatus();

        SubscribeResponseDTO subscribeResponseDTO = new SubscribeResponseDTO(uuid, event, code, status);

        return subscribeResponseDTO;




    }
}
