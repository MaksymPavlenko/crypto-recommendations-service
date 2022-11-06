package com.crs.cryptorecommendationsservice.repository;

import com.crs.cryptorecommendationsservice.exception.NoPricesFoundException;
import com.crs.cryptorecommendationsservice.exception.UnsupportedCryptocurrencyException;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static com.crs.cryptorecommendationsservice.TestUtils.createCryptoPrice;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CryptoPricesRepositoryTest {

    private static final String BTC = "BTC", ETH = "ETH", XRP = "XRP";

    @Test
    void shouldReturnSymbols() {
        // Given
        val repository = new CryptoPricesRepository(Map.of(BTC, List.of(), ETH, List.of(), XRP, List.of()));

        // When
        val symbols = repository.getSymbols();

        // Then
        assertThat(symbols).containsExactlyInAnyOrder(BTC, ETH, XRP);
    }

    @Test
    void shouldReturnMinPrice() {
        // Given
        val minPrice = createCryptoPrice(1);
        val repository = new CryptoPricesRepository(Map.of(
                BTC,
                List.of(createCryptoPrice(10), minPrice, createCryptoPrice(1))
        ));

        // When Then
        assertThat(repository.getMinPrice(BTC)).isEqualTo(minPrice);
    }

    @Test
    void shouldReturnMaxPrice() {
        // Given
        val maxPrice = createCryptoPrice(10);
        val repository = new CryptoPricesRepository(Map.of(
                BTC,
                List.of(createCryptoPrice(0), maxPrice, createCryptoPrice(1))
        ));

        // When Then
        assertThat(repository.getMaxPrice(BTC)).isEqualTo(maxPrice);
    }

    @Test
    void shouldReturnOldestPrice() {
        // Given
        val oldestPrice = createCryptoPrice(1, -5);
        val repository = new CryptoPricesRepository(Map.of(
                BTC,
                List.of(oldestPrice, createCryptoPrice(10), createCryptoPrice(1))
        ));

        // When Then
        assertThat(repository.getOldestPrice(BTC)).isEqualTo(oldestPrice);
    }

    @Test
    void shouldReturnNewestPrice() {
        // Given
        val newestPrice = createCryptoPrice(10, 5);
        val repository = new CryptoPricesRepository(Map.of(
                BTC,
                List.of(createCryptoPrice(0), createCryptoPrice(1), newestPrice)
        ));

        // When Then
        assertThat(repository.getMaxPrice(BTC)).isEqualTo(newestPrice);
    }

    @Test
    void shouldThrowUnsupportedCryptocurrencyExceptionGivenUnknownCryptocurrency() {
        // Given
        val repository = new CryptoPricesRepository(Map.of(BTC, List.of()));

        // When Then
        assertThatThrownBy(() -> repository.getValidPrices(XRP)).isInstanceOf(UnsupportedCryptocurrencyException.class);
    }

    @Test
    void shouldThrowNoPricesFoundExceptionGivenNoPricesForSpecifiedCryptocurrency() {
        // Given
        val repository = new CryptoPricesRepository(Map.of(BTC, List.of()));

        // When Then
        assertThatThrownBy(() -> repository.getValidPrices(BTC)).isInstanceOf(NoPricesFoundException.class);
    }
}