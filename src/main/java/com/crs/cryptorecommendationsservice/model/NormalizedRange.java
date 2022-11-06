package com.crs.cryptorecommendationsservice.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * Normalized cryptocurrency range
 *
 * @param symbol cryptocurrency symbol i.e. BTC, ETH
 * @param value  equals to (max - min) / min of cryptocurrency price value during a certain period.
 *               Is an indicator of volatility
 */
public record NormalizedRange(
        @Schema(example = "BTC") String symbol,
        @Schema(example = "21196.42") BigDecimal value) {
}