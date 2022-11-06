package com.crs.cryptorecommendationsservice;

import com.crs.cryptorecommendationsservice.model.CryptoPrice;

import java.math.BigDecimal;
import java.time.Instant;

public class TestUtils {

    public static CryptoPrice createCryptoPrice(double value) {
        return new CryptoPrice(Instant.now(), BigDecimal.valueOf(value));
    }

    public static CryptoPrice createCryptoPrice(double value, int secondsChange) {
        return new CryptoPrice(Instant.now().plusSeconds(secondsChange), BigDecimal.valueOf(value));
    }

    public static CryptoPrice createCryptoPrice(double value, String date) {
        return new CryptoPrice(Instant.parse(date), BigDecimal.valueOf(value));
    }
}
