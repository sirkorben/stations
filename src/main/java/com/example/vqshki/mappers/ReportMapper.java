package com.example.vqshki.mappers;

import com.example.vqshki.dto.ReportDTO;
import com.example.vqshki.models.Report;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReportMapper {
    List<ReportDTO> reportToReportDTO(List<Report> ms);
}
