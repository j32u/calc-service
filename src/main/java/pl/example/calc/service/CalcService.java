package pl.example.calc.service;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pl.example.calc.dto.SeriesDto;
import pl.example.calc.dto.SeriesType;

import java.util.Iterator;

@Service
public class CalcService {

	private final ArithmeticSeriesService arithmeticSeriesService;

	public CalcService(ArithmeticSeriesService arithmeticSeriesService) {
        this.arithmeticSeriesService = arithmeticSeriesService;
    }

	public void validate(SeriesDto dto) {
		if (SeriesType.geometric.equals(dto.getType())) {
			validateGeometric(dto);
		} else if (SeriesType.arithmetic.equals(dto.getType())) {
			validateArithmetic(dto);
		}
	}

	public SeriesDto createNewSeriesWithAdditionalElements(SeriesDto dto, int n) {
		if (SeriesType.geometric.equals(dto.getType())) {
			throw new IllegalArgumentException("not supported, geometric");
		} else if (SeriesType.arithmetic.equals(dto.getType())) {
			return getNewSeriesWithAdditionalElements(dto, n);
		}
		throw new IllegalArgumentException("not supported, unknown series type");
	}

	public SeriesDto createRandomNewSeries(SeriesType seriesType) {
		if (SeriesType.geometric.equals(seriesType)) {
			throw new IllegalArgumentException("not supported, geometric");
		}  else if (SeriesType.arithmetic.equals(seriesType)) {
			return arithmeticSeriesService.createRandomNewSeries();
		}
		throw new IllegalArgumentException("not supported, unknown series type");
	}

	protected SeriesDto getNewSeriesWithAdditionalElements(SeriesDto dto, int n) {
		return arithmeticSeriesService.createNewSeriesWithAdditionalElements(dto, n);
	}

	protected void validateGeometric(SeriesDto dto) {
		throw new IllegalArgumentException("not supported");
	}

	protected void validateArithmetic(SeriesDto dto) {
		arithmeticSeriesService.validate(dto);
	}

}
