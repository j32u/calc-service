package pl.example.calc.controller;

import org.springframework.web.bind.annotation.RestController;
import pl.example.calc.annotation.SeriesApiExceptionMapper;
import pl.example.calc.dto.SeriesDto;
import pl.example.calc.dto.SeriesType;
import pl.example.calc.service.SeriesService;

@RestController
@SeriesApiExceptionMapper
public class SeriesController implements SeriesAPI {

	private final SeriesService seriesService;

	public SeriesController(SeriesService seriesService) {
		this.seriesService = seriesService;
	}

	@Override
	public SeriesDto createRandomNewSeries(SeriesType seriesType) {
		return seriesService.createRandomNewSeries(seriesType);
	}

	@Override
	public SeriesDto createRandomNewSeries(SeriesDto dto, int n) {
		return seriesService.createNewSeriesWithAdditionalElements(dto, n);
	}

}
