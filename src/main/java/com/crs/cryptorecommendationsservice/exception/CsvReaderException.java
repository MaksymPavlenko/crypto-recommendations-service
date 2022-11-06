package com.crs.cryptorecommendationsservice.exception;

/**
 * Exception caused by errors during loading of cryptocurrency prices data from a CSV resource file
 */
public class CsvReaderException extends RuntimeException {

    public CsvReaderException(final String message) {
        super(message);
    }

    public CsvReaderException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
