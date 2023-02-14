package com.example.vqshki.mappers;

import com.example.vqshki.dto.MobileStationDTO;
import com.example.vqshki.models.MobileStation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MobileStationMapper {
    MobileStationDTO mobileStationToDto(MobileStation ms);
}
