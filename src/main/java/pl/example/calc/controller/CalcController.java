package pl.example.calc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.example.calc.dto.SeriesDto;
import pl.example.calc.dto.SeriesType;
import pl.example.calc.service.CalcService;

@RestController
@RequestMapping("/api/calc")
public class CalcController {

	private final CalcService calcService;

	public CalcController(CalcService calcService) {
		this.calcService = calcService;
	}

	@GetMapping("/series/random")
	public SeriesDto createRandomNewSeries(@RequestParam("type") SeriesType seriesType) {
		return calcService.createRandomNewSeries(seriesType);
	}

	@PostMapping("/series/extend")
	public SeriesDto createRandomNewSeries(@RequestBody SeriesDto dto, @RequestParam("n") int n) {
		return calcService.createNewSeriesWithAdditionalElements(dto, n);
	}

}
