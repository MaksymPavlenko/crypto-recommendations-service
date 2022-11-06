package com.crs.cryptorecommendationsservice.repository;

import com.crs.cryptorecommendationsservice.model.CryptoPrice;

import java.math.BigDecimal;
import java.util.*;

public class CryptoPricesRepository {

    private final Map<String, List<CryptoPrice>> cryptoPrices;

    public CryptoPricesRepository(final Map<String, List<CryptoPrice>> cryptoPrices) {
        this.cryptoPrices = cryptoPrices;
    }

    /**
     * @return a set of all supported cryptocurrency symbols i.e. BTC, ETH
     */
    public Set<String> getCryptos() {
        return cryptoPrices.keySet();
    }

    /**
     * @param crypto cryptocurrency to find price's value for
     * @return the minimal value of cryptocurrency's price for a certain period of time
     * @throws NoSuchElementException if there are no price values
     */
    public BigDecimal getMinPrice(final String crypto) {
        return getValidatedPrices(crypto).stream()
                .min(Comparator.comparing(CryptoPrice::price))
                .orElseThrow(NoSuchElementException::new)
                .price();
    }

    /**
     * @param crypto cryptocurrency to find price's value for
     * @return the maximal value of cryptocurrency's price for a certain period of time
     * @throws NoSuchElementException if there are no price values
     */
    public BigDecimal getMaxPrice(final String crypto) {
        return getValidatedPrices(crypto).stream()
                .max(Comparator.comparing(CryptoPrice::price))
                .orElseThrow(NoSuchElementException::new)
                .price();
    }

    private List<CryptoPrice> getValidatedPrices(final String crypto) {
        if (cryptoPrices.containsKey(crypto)) {
            return cryptoPrices.get(crypto);
        } else {
            throw new IllegalArgumentException("Such crypto currency is not supported");
        }
    }
}
