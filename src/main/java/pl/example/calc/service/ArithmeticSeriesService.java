package pl.example.calc.service;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pl.example.calc.dto.SeriesDto;
import pl.example.calc.dto.SeriesType;

import java.util.Iterator;
import java.util.List;
import java.util.random.RandomGenerator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class ArithmeticSeriesService {

    public static final String ARITHMETIC_SERIES_MUST_HAVE_AT_LEAST_ONE_VALUE = "arithmetic series must have at least one value";
    public static final String SUM_FORMULA_IS_INCORRECT = "Sum formula is incorrect";
    public static final String INCORRECT_ELEMENT_AT_POSITION_D = "incorrect element at position %d";
    public static final String TWO_VALUES_NEEDED_FOR_ARITHMETIC_SERIES_IN_ORDER_TO_CALCULATE_STEP = "two values needed for arithmetic series in order to calculate step";
    public static final String INVALID_ARITHMETIC_SERIES_TYPE = "invalid arithmetic series type";

    public void validate(SeriesDto dto) {
        validateIsArithmetic(dto);
        validateNotEmpty(dto);
        if (isSingleElement(dto)) {
            return;
        }
        validateSumFormulaOnSeries(dto);
        validateStep(dto);
    }

    private void validateIsArithmetic(SeriesDto dto) {
        if (!SeriesType.arithmetic.equals(dto.type())) {
            throw new IllegalArgumentException(INVALID_ARITHMETIC_SERIES_TYPE);
        }
    }

    public SeriesDto createNewSeriesWithAdditionalElements(SeriesDto dto, int n) {
        validate(dto);
        validateHasAtLeastTwoElements(dto);
        return SeriesDto.builder().type(SeriesType.arithmetic).values(getUpdatedSeries(dto, n)).build();
    }

    public SeriesDto createRandomNewSeries() {
        RandomGenerator randomGenerator = RandomGenerator.getDefault();
        SeriesDto dto = SeriesDto.builder()
                .type(SeriesType.arithmetic)
                .values(List.of(Double.valueOf(randomGenerator.nextInt(-10, 10)), Double.valueOf(randomGenerator.nextInt(-10, 10))))
                .build();
        return createNewSeriesWithAdditionalElements(dto, randomGenerator.nextInt(5, 10));
    }

    private static @NonNull List<Double> getUpdatedSeries(SeriesDto dto, int n) {
        return Stream.concat(dto.values().stream(), generateNextValuesStream(getLast(dto), calculateStep(dto), n)).toList();
    }

    private static @NonNull Stream<Double> generateNextValuesStream(Double element, double step, int count) {
        return IntStream.range(1, count).mapToDouble(value -> element + value * step).boxed();
    }

    private void validateHasAtLeastTwoElements(SeriesDto dto) {
        if (dto.values().size() < 2) {
            throw new IllegalArgumentException(TWO_VALUES_NEEDED_FOR_ARITHMETIC_SERIES_IN_ORDER_TO_CALCULATE_STEP);
        }
    }

    private void validateStep(SeriesDto dto) {
        double step = calculateStep(dto);
        Double first = getFirst(dto);
        Iterator<Double> it = dto.values().iterator();
        int i = 0;
        while (it.hasNext()) {
            if (!it.next().equals(first + i++ * step)) {
                throw new IllegalArgumentException(INCORRECT_ELEMENT_AT_POSITION_D.formatted(i));
            }
        }
    }

    private static double calculateStep(SeriesDto dto) {
        return getSecond(dto) - getFirst(dto);
    }

    private void validateSumFormulaOnSeries(SeriesDto dto) {
        if (!calculateSumFromSeries(dto).equals(calculateSumFormula(dto))) {
            throw new IllegalArgumentException(SUM_FORMULA_IS_INCORRECT);
        }
    }

    private static boolean isSingleElement(SeriesDto dto) {
        return getAmount(dto) == 1;
    }

    private static void validateNotEmpty(SeriesDto dto) {
        if (CollectionUtils.isEmpty(dto.values())) {
            throw new IllegalArgumentException(ARITHMETIC_SERIES_MUST_HAVE_AT_LEAST_ONE_VALUE);
        }
    }

    private static double calculateSumFormula(SeriesDto dto) {
        return (getFirst(dto) + getLast(dto)) * getAmount(dto) / 2;
    }

    private static @NonNull Double calculateSumFromSeries(SeriesDto dto) {
        return dto.values().stream().reduce((sum, next) -> sum + next).orElseThrow();
    }

    private static int getAmount(SeriesDto dto) {
        return dto.values().size();
    }

    private static Double getSecond(SeriesDto dto) {
        return dto.values().get(1);
    }

    private static Double getFirst(SeriesDto dto) {
        return dto.values().get(0);
    }

    private static @NonNull Double getLast(SeriesDto dto) {
        return dto.values().stream().reduce((a, b) -> b).orElseThrow();
    }

}
