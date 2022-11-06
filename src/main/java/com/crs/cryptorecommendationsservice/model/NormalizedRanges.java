package com.crs.cryptorecommendationsservice.model;

import java.util.List;

/**
 * @param normalizedRanges a list of normalized cryptocurrency ranges
 *                         sorted in descending order by normalized range value
 */
public record NormalizedRanges(List<NormalizedRange> normalizedRanges) {
}