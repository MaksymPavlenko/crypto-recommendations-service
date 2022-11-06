package com.crs.cryptorecommendationsservice.repository;

import com.crs.cryptorecommendationsservice.exception.NoPricesFoundException;
import com.crs.cryptorecommendationsservice.exception.UnsupportedCryptocurrencyException;
import com.crs.cryptorecommendationsservice.model.CryptoPrice;
import com.google.common.annotations.VisibleForTesting;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class CryptoPricesRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(CryptoPricesRepository.class);
    private final Map<String, List<CryptoPrice>> cryptoPrices;

    public CryptoPricesRepository(final Map<String, List<CryptoPrice>> cryptoPrices) {
        this.cryptoPrices = cryptoPrices;
    }

    /**
     * @return a set of all supported cryptocurrency symbols i.e. BTC, ETH
     */
    public Set<String> getSymbols() {
        return cryptoPrices.keySet();
    }

    /**
     * @param symbol cryptocurrency to find price value for
     * @return the minimal value of cryptocurrency price value for a certain period of time
     * @throws NoSuchElementException if there are no value values
     */
    public CryptoPrice getMinPrice(final String symbol) {
        return getValidPrices(symbol).stream()
                .min(Comparator.comparing(CryptoPrice::value))
                .get();
    }

    /**
     * @param symbol cryptocurrency to find price value for
     * @return the maximal value of cryptocurrency price value for a certain period of time
     * @throws NoSuchElementException if there are no value values
     */
    public CryptoPrice getMaxPrice(final String symbol) {
        return getValidPrices(symbol).stream()
                .max(Comparator.comparing(CryptoPrice::value))
                .get();
    }

    /**
     * @param symbol cryptocurrency to find price value for
     * @return the oldest available value of cryptocurrency price value for a certain period of time
     * @throws NoSuchElementException if there are no value values
     */
    public CryptoPrice getOldestPrice(final String symbol) {
        val validPrices = getValidPrices(symbol);
        return validPrices.get(0);
    }

    /**
     * @param symbol cryptocurrency to find price value for
     * @return the oldest available value of cryptocurrency price value for a certain period of time
     * @throws NoSuchElementException if there are no value values
     */
    public CryptoPrice getNewestPrice(final String symbol) {
        val validPrices = getValidPrices(symbol);
        return validPrices.get(validPrices.size() - 1);
    }

    @VisibleForTesting
    List<CryptoPrice> getValidPrices(final String symbol) {
        if (!cryptoPrices.containsKey(symbol)) {
            LOGGER.error("{} cryptocurrency is not supported", symbol);
            throw new UnsupportedCryptocurrencyException(symbol);
        }
        val prices = cryptoPrices.get(symbol);
        if (prices.isEmpty()) {
            LOGGER.error("The prices data for {} was not found", symbol);
            throw new NoPricesFoundException(symbol);
        }
        return prices;
    }
}
