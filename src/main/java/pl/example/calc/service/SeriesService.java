package pl.example.calc.service;

import org.springframework.stereotype.Service;
import pl.example.calc.dto.SeriesDto;
import pl.example.calc.dto.SeriesType;

@Service
public class SeriesService {

	private final ArithmeticSeriesService arithmeticSeriesService;

	public SeriesService(ArithmeticSeriesService arithmeticSeriesService) {
        this.arithmeticSeriesService = arithmeticSeriesService;
    }

	public void validate(SeriesDto dto) {
        switch (dto.type()) {
            case arithmetic -> arithmeticSeriesService.validate(dto);
			case geometric -> throw new IllegalArgumentException("not supported");
        }
	}

	public SeriesDto createNewSeriesWithAdditionalElements(SeriesDto dto, int n) {
		return switch (dto.type()) {
			case arithmetic ->  arithmeticSeriesService.createNewSeriesWithAdditionalElements(dto, n);
			case geometric -> throw new IllegalArgumentException("not supported, geometric");
		};
	}

	public SeriesDto createRandomNewSeries(SeriesType seriesType) {
		return switch (seriesType) {
			case arithmetic ->  arithmeticSeriesService.createRandomNewSeries();
			case geometric -> throw new IllegalArgumentException("not supported, geometric");
		};
	}

}
