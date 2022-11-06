package com.crs.cryptorecommendationsservice.model;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * @param timestamp the specified point of time
 * @param value     cryptocurrency price value at the specified point of time
 */
public record CryptoPrice(Instant timestamp, BigDecimal value) {
}
