package com.example.vqshki.service;

import com.example.vqshki.repository.ReportRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.example.vqshki.constants.TestConstants.TEST_BS_REQUEST_MSG;
import static com.example.vqshki.constants.TestConstants.TEST_MODIFIED_REPORT_LIST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BaseStationServiceTest {
    @Mock
    private ReportRepository mockedReportRepository;
    @Test
    public void shouldReturnSavedReports() {
        when(mockedReportRepository.saveAll(TEST_MODIFIED_REPORT_LIST)).thenReturn(TEST_MODIFIED_REPORT_LIST);

        BaseStationService baseStationService = new BaseStationService(mockedReportRepository);

        assertEquals(TEST_MODIFIED_REPORT_LIST, baseStationService.saveReports(TEST_BS_REQUEST_MSG));
    }
}