package com.crs.cryptorecommendationsservice.controller;

import com.crs.cryptorecommendationsservice.model.CryptocurrencyDetails;
import com.crs.cryptorecommendationsservice.model.NormalizedRanges;
import com.crs.cryptorecommendationsservice.service.RecommendationsService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecommendationsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecommendationsController.class);
    private final RecommendationsService service;

    public RecommendationsController(final RecommendationsService service) {
        this.service = service;
    }

    @GetMapping("/v1/normalizedRanges")
    @ApiResponse(description = "A sorted list of cryptocurrencies" +
            " with their normalized range values (i.e. (max - min) / min) " +
            "for a certain period of time in descending order")
    public NormalizedRanges getNormalizedRanges() {
        LOGGER.info("Getting normalized ranges");
        return service.getNormalizedRanges();
    }

    @GetMapping("/v1/crypto/{symbol}")
    public CryptocurrencyDetails getCryptocurrencyDetails(@PathVariable final String symbol) {
        LOGGER.info("Getting {} details", symbol);
        return service.getCryptocurrencyDetails(symbol);
    }
}
