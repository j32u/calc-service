package pl.example.calc.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.example.calc.dto.SeriesDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ANNOTATED)
public class CalcControllerIntegrationTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Autowired
    public CalcControllerIntegrationTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    public void test() throws Exception {
        int limit = 3;

        MvcResult seriesRandomResult = mockMvc.perform(get("/api/calc/series/random").param("type", "arithmetic"))
                .andExpect(status().isOk()).andReturn();
        SeriesDto seriesRandomDto = objectMapper.readValue(seriesRandomResult.getResponse().getContentAsString(), SeriesDto.class);

        MvcResult seriesExtendResult = mockMvc.perform(post("/api/calc/series/extend")
                        .param("n", Integer.toString(limit))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(seriesRandomDto)))
                .andExpect(status().isOk()).andReturn();
        SeriesDto seriesExtendedDto = objectMapper.readValue(seriesExtendResult.getResponse().getContentAsString(), SeriesDto.class);

        Assertions.assertThat(seriesExtendedDto.values()).isNotEmpty().hasSize(seriesRandomDto.values().size() + limit);
    }

}
