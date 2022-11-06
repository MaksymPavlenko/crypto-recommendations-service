package com.crs.cryptorecommendationsservice.model;

public record CryptocurrencyDetails(String symbol,
                                    CryptoPrice oldestPrice,
                                    CryptoPrice newestPrice,
                                    CryptoPrice minPrice,
                                    CryptoPrice maxPrice) {
}
