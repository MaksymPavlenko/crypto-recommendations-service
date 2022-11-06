package com.crs.cryptorecommendationsservice.error;

import com.crs.cryptorecommendationsservice.exception.NoPricesFoundException;
import com.crs.cryptorecommendationsservice.exception.UnsupportedCryptocurrencyException;
import com.crs.cryptorecommendationsservice.model.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ErrorsHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorsHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception e) {
        LOGGER.error("Unhandled error", e);
        return new ErrorResponse(getFormattedStatus(HttpStatus.INTERNAL_SERVER_ERROR), "Internal server error");
    }

    @ExceptionHandler(UnsupportedCryptocurrencyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(UnsupportedCryptocurrencyException e) {
        LOGGER.error(e.getMessage(), e);
        return new ErrorResponse(getFormattedStatus(HttpStatus.BAD_REQUEST), e.getMessage());
    }

    @ExceptionHandler(NoPricesFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(NoPricesFoundException e) {
        LOGGER.error(e.getMessage(), e);
        return new ErrorResponse(getFormattedStatus(HttpStatus.INTERNAL_SERVER_ERROR), e.getMessage());
    }

    private String getFormattedStatus(final HttpStatus status) {
        return status.value() + " " + status.getReasonPhrase();
    }
}
