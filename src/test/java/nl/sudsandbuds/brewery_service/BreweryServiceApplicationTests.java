package nl.sudsandbuds.brewery_service;

import nl.sudsandbuds.brewery_service.controllers.BreweryController;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BreweryServiceApplicationTests {
	@Autowired
	BreweryController breweryController;

	@Autowired
	MockMvc mockMvc;
	@Test
	void contextLoads() {
	}

	@Test
	void testBreweryList() throws Exception {
		ResultActions result = mockMvc.perform(get("/brewery/"));

		result.andExpect(status().isOk());
	}

	@Test
	void testBreweryLimit() throws Exception {
		ResultActions result = mockMvc.perform(get("/brewery/?limit={limit}", 10));
		result.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.payload").value(Matchers.hasSize(10)));
	}

	@Test
	void testValidationExceptionOnBreweryListLimit() throws Exception {
		Integer limit = 70;
		ResultActions result = mockMvc.perform(get("/brewery/?limit={limit}", 70));

		result.andExpect(status().isBadRequest());
	}

}
