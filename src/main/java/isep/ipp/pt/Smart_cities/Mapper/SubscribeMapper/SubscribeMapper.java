package isep.ipp.pt.Smart_cities.Mapper.SubscribeMapper;

import isep.ipp.pt.Smart_cities.Dto.SubscribeDto.SubscribeResponseDTO;
import isep.ipp.pt.Smart_cities.Model.Subscribe;

public interface SubscribeMapper {
    SubscribeResponseDTO toSubscribeResponseDTO(Subscribe subscribel);
}
