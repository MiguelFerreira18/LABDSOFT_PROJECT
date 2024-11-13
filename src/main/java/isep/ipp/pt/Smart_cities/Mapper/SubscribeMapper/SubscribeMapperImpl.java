package isep.ipp.pt.Smart_cities.Mapper.SubscribeMapper;

import isep.ipp.pt.Smart_cities.Dto.SubscribeDto.SubscribeResponseDTO;
import isep.ipp.pt.Smart_cities.Model.EventModel.Event;
import isep.ipp.pt.Smart_cities.Model.Subscribe;
import isep.ipp.pt.Smart_cities.Model.SubscriptionStatus;
import org.springframework.stereotype.Component;

@Component
public class SubscribeMapperImpl implements SubscribeMapper {

    @Override
    public SubscribeResponseDTO toSubscribeResponseDTO(Subscribe subscribe) {
        if (subscribe == null) {
            return null;
        }


        Long id = subscribe.getId();
        Event event = subscribe.getEvent();
        int code = subscribe.getCode();
        SubscriptionStatus status = subscribe.getSubscriptionStatus();

        SubscribeResponseDTO subscribeResponseDTO = new SubscribeResponseDTO(id, event, code, status);

        return subscribeResponseDTO;
    }


}
