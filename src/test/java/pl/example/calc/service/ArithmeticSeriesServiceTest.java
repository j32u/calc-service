package pl.example.calc.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.example.calc.dto.SeriesDto;
import pl.example.calc.dto.SeriesType;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ArithmeticSeriesServiceTest {

    @InjectMocks
    private ArithmeticSeriesService service;

    private static SeriesDto createArithmeticSeries(List<Double> values) {
        return SeriesDto.builder().type(SeriesType.arithmetic).values(values).build();
    }

    @Test
    public void shouldPass() {
        SeriesDto dto = createArithmeticSeries(List.of(1., 2., 3.));
        Assertions.assertThatCode(() -> service.validate(dto)).doesNotThrowAnyException();
    }

    @Test
    public void shouldThrowWhenNull() {
        SeriesDto dto = createArithmeticSeries(null);
        Assertions.assertThatThrownBy(() -> service.validate(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ArithmeticSeriesService.ARITHMETIC_SERIES_MUST_HAVE_AT_LEAST_ONE_VALUE);
    }

    @Test
    public void shouldThrowWhenEmpty() {
        SeriesDto dto = createArithmeticSeries(new ArrayList<>(0));
        Assertions.assertThatThrownBy(() -> service.validate(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ArithmeticSeriesService.ARITHMETIC_SERIES_MUST_HAVE_AT_LEAST_ONE_VALUE);
    }

    @Test
    public void shouldPassWithSingleElement() {
        SeriesDto dto = createArithmeticSeries(List.of(1.));
        Assertions.assertThatCode(() -> service.validate(dto)).doesNotThrowAnyException();
    }

    @Test
    public void shouldThrowWhenSimpleCheckBySum() {
        SeriesDto dto = createArithmeticSeries(List.of(1., 2., 3., 4., 4.));
        Assertions.assertThatThrownBy(() -> service.validate(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ArithmeticSeriesService.SUM_FORMULA_IS_INCORRECT);
    }
    @Test
    public void shouldThrowWhenIncorrectStepAtPosition() {
        SeriesDto dto = createArithmeticSeries(List.of(1., 2., 4., 3., 5.));
        Assertions.assertThatThrownBy(() -> service.validate(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ArithmeticSeriesService.INCORRECT_ELEMENT_AT_POSITION_D.formatted(3));
    }

}
