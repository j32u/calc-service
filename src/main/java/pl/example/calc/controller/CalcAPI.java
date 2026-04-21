package pl.example.calc.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.example.calc.dto.SeriesDto;
import pl.example.calc.dto.SeriesType;

@Tag(name = "Ciągi", description = "Endpoint dedykowany na ciągi liczbowe")
@RequestMapping("/api/calc")
public interface CalcAPI {

    @Operation(summary = "Generuj ciąg liczbowy", description = "Generuje ciąg liczbowy na podstawie typu ciągu.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "400", description = "Błąd walidacji")
    })
    @GetMapping("/series/random")
    SeriesDto createRandomNewSeries(
            @Parameter(description = "Typ generowanego ciągu liczbowego")
            @RequestParam("type") SeriesType seriesType);


    @Operation(summary = "Rozwiń ciąg liczbowy", description = "Generuje ciąg liczbowy na podstawie ciągu liczbowego.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "400", description = "Błąd walidacji")
    })
    @PostMapping("/series/extend")
    SeriesDto createRandomNewSeries(
            @Parameter(description = "Wzorcowy ciąg liczbowy")
            @Valid @RequestBody SeriesDto dto,

            @Parameter(description = "Ilość kolejnych elementów ciągu")
            @RequestParam(value = "n", defaultValue = "1") int n);

}
