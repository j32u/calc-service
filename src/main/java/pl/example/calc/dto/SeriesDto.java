package pl.example.calc.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record SeriesDto(

        @NotNull(message = "Type can not be null")
        SeriesType type,

        @NotNull(message = "Values can not be null")
        @NotEmpty(message = "Values can not be empty")
        List<Double> values) {
}
