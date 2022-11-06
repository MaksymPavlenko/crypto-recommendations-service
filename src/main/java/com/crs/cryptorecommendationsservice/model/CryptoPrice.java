package com.crs.cryptorecommendationsservice.model;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * @param timestamp
 * @param price     cryptocurrency value at the specified point of time
 */
public record CryptoPrice(Instant timestamp, BigDecimal price) {
}
