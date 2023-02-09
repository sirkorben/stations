package com.example.vqshki;

import com.example.vqshki.models.Report;
import com.example.vqshki.utils.BaseStationRequestMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static com.example.vqshki.constants.TestConstants.TEST_BASE_STATION_A;
import static com.example.vqshki.constants.TestConstants.TEST_MOBILE_STATION_Z;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = VqshkiApplication.class)
@AutoConfigureMockMvc
class VqshkiApplicationTests {

	@Autowired
	private MockMvc mvc;

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void createReportSuccess() throws Exception {

		Report report = new Report();
		report.setMobileStationId(TEST_MOBILE_STATION_Z);
		report.setDistance(10);
		report.setTimeDetected(Timestamp.from(Instant.now()));

		BaseStationRequestMessage baseStationRequestMessage = new BaseStationRequestMessage(TEST_BASE_STATION_A, List.of(report));

		mvc.perform(MockMvcRequestBuilders.post("/api/v1/report")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(baseStationRequestMessage)))
				.andExpect(status().isCreated());

	}

	@Test
	public void getMobileStationPositionTest() throws Exception {

		mvc.perform(get("/api/v1/location/" + TEST_MOBILE_STATION_Z)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().string(containsString(TEST_MOBILE_STATION_Z.toString())));
	}
}
