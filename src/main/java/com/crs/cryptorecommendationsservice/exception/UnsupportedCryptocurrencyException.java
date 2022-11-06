package com.crs.cryptorecommendationsservice.exception;

public class UnsupportedCryptocurrencyException extends RuntimeException {

    public UnsupportedCryptocurrencyException(final String symbol) {
        super("The " + symbol + " cryptocurrency is not supported");
    }
}
