package pl.example.calc.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class HeaderIntegrationTest {

    public static final String URL = "http://127.0.0.1:%d/api/calc/series/random";
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldContainTomcatDefaultHeaders() {
        ResponseEntity<String> response = restTemplate.getForEntity(URL.formatted(port), String.class);

        HttpHeaders headers = response.getHeaders();
        Assertions.assertThat(headers).isNotNull();

        Assertions.assertThat(headers).containsKey(HttpHeaders.DATE).extracting(HttpHeaders.DATE).isNotNull();

        expectDateHeader(headers.getFirst(HttpHeaders.DATE));

    }

    private static void expectDateHeader(String header) {
        Assertions.assertThat(header).isNotNull();

        Instant instant = ZonedDateTime.parse(header, DateTimeFormatter.RFC_1123_DATE_TIME).toInstant();
        long seconds = Duration.between(instant, Instant.now()).toSeconds();

        Assertions.assertThat(seconds).isLessThan(10);
    }
}
