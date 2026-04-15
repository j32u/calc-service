package pl.example.calc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.example.calc.annotation.ApiCalcExceptionMapper;
import tools.jackson.databind.exc.InvalidFormatException;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice(annotations = ApiCalcExceptionMapper.class)
public class CalcExceptionHandler {

    @ExceptionHandler(value = {IllegalArgumentException.class, NoSuchElementException.class})
    public ProblemDetail handleIllegalArgument(IllegalArgumentException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("Bad Request");
        problemDetail.setProperty("errorCode", "INVALID_ARGUMENT");
        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {

        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .toList();

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed");
        problemDetail.setTitle("Bad Request");
        problemDetail.setProperty("errorCode", "VALIDATION_FAILED");
        problemDetail.setProperty("errors", errors);

        return problemDetail;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Invalid request content"
        );
        problemDetail.setTitle("Bad Request");

        if (ex.getCause() instanceof InvalidFormatException jme) {
            if (!CollectionUtils.isEmpty(jme.getPath())) {
                String propertyName = jme.getPath().get(jme.getPath().size()-1).getPropertyName();
                String message = "Invalid value for field '%s': %s. Allowed values: %s";
                problemDetail.setDetail(message.formatted(propertyName, jme.getValue(), getAllowedEnumValues(jme)));
                problemDetail.setProperty("errorCode", "INVALID_ENUM_VALUE");
            }
            else {
                problemDetail.setDetail("Could not read JSON: " + jme.getOriginalMessage());
                problemDetail.setProperty("errorCode", "JSON_MAPPING_ERROR");
            }
        }
        else {
            problemDetail.setDetail(ex.getMessage());
            problemDetail.setProperty("errorCode", "INVALID_REQUEST_CONTENT");
        }

        return problemDetail;
    }

    private String getAllowedEnumValues(InvalidFormatException ex) {
        Class<?> targetType = ex.getTargetType();
        if (targetType != null && targetType.isEnum()) {
            return Arrays.stream(targetType.getEnumConstants())
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));
        }
        return "[unknown]";
    }
}
