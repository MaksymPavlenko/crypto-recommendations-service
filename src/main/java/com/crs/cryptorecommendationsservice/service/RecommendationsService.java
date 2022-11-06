package com.crs.cryptorecommendationsservice.service;

import com.crs.cryptorecommendationsservice.model.CryptocurrencyDetails;
import com.crs.cryptorecommendationsservice.model.NormalizedRange;
import com.crs.cryptorecommendationsservice.model.NormalizedRanges;
import com.crs.cryptorecommendationsservice.repository.CryptoPricesRepository;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

/**
 * Service to execute calculations and build response models for cryptocurrencies to invest into
 */
@Service
public class RecommendationsService {

    private final CryptoPricesRepository repository;
    private final int scale;

    public RecommendationsService(CryptoPricesRepository repository, @Value("${calculations.scale}") int scale) {
        this.repository = repository;
        this.scale = scale;
    }

    /**
     * @return An immutable list of normalized ranges for the supported cryptocurrencies.
     * Normalized range is a value equal to (max - min) / min value of a cryptocurrency for a certain period of time.
     * The list is sorted in normalized range descending order showing the most volatile cryptocurrencies at the top.
     */
    public NormalizedRanges getNormalizedRanges() {
        List<NormalizedRange> normalizedRanges = repository.getSymbols().stream()
                .map(this::getNormalizedRange)
                .sorted(Comparator.comparing(NormalizedRange::value).reversed())
                .toList();

        return new NormalizedRanges(normalizedRanges);
    }

    private NormalizedRange getNormalizedRange(String symbol) {
        val minPrice = repository.getMinPrice(symbol).value();
        val maxPrice = repository.getMaxPrice(symbol).value();
        val value = maxPrice.subtract(minPrice).divide(minPrice, scale, RoundingMode.HALF_UP).stripTrailingZeros();
        return new NormalizedRange(symbol, value);
    }

    public CryptocurrencyDetails getCryptocurrencyDetails(final String symbol) {
        return new CryptocurrencyDetails(
                symbol,
                repository.getOldestPrice(symbol),
                repository.getNewestPrice(symbol),
                repository.getMinPrice(symbol),
                repository.getMaxPrice(symbol)
        );
    }
}
