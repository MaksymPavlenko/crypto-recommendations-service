package com.crs.cryptorecommendationsservice.exception;

public class NoPricesFoundException extends RuntimeException {

    public NoPricesFoundException(final String symbol) {
        super("The prices data for " + symbol + " was not found");
    }
}
