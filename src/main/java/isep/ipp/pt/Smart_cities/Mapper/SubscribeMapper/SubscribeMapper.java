package isep.ipp.pt.Smart_cities.Mapper.SubscribeMapper;

import isep.ipp.pt.Smart_cities.Dto.SubscribeDto.SubscribeRequestDTO;
import isep.ipp.pt.Smart_cities.Dto.SubscribeDto.SubscribeResponseDTO;
import isep.ipp.pt.Smart_cities.Model.Subscribe;
import isep.ipp.pt.Smart_cities.Util.EncryptionUtil;

public interface SubscribeMapper {
    SubscribeResponseDTO toSubscribeResponseDTO(Subscribe subscribe, EncryptionUtil util);
}
