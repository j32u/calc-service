package pl.example.calc.controller;

import org.springframework.web.bind.annotation.RestController;
import pl.example.calc.annotation.ApiCalcExceptionMapper;
import pl.example.calc.dto.SeriesDto;
import pl.example.calc.dto.SeriesType;
import pl.example.calc.service.CalcService;

@RestController
@ApiCalcExceptionMapper
public class CalcController implements CalcAPI{

	private final CalcService calcService;

	public CalcController(CalcService calcService) {
		this.calcService = calcService;
	}

	@Override
	public SeriesDto createRandomNewSeries(SeriesType seriesType) {
		return calcService.createRandomNewSeries(seriesType);
	}

	@Override
	public SeriesDto createRandomNewSeries(SeriesDto dto, int n) {
		return calcService.createNewSeriesWithAdditionalElements(dto, n);
	}

}
