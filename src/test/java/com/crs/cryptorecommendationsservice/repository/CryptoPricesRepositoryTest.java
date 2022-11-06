package com.crs.cryptorecommendationsservice.repository;

import com.crs.cryptorecommendationsservice.model.CryptoPrice;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;

class CryptoPricesRepositoryTest {

    private static final String BTC = "BTC", ETH = "ETH", XRP = "XRP";

    @Test
    void shouldReturnCryptos() {
        // Given
        val repository = new CryptoPricesRepository(Map.of(BTC, List.of(), ETH, List.of(), XRP, List.of()));

        // When
        val cryptos = repository.getCryptos();

        // Then
        assertThat(cryptos).containsExactlyInAnyOrder(BTC, ETH, XRP);
    }

    @Test
    void shouldReturnMinPrice() {
        // Given
        val repository = new CryptoPricesRepository(Map.of(
                BTC,
                List.of(
                        new CryptoPrice(Instant.EPOCH, BigDecimal.ONE),
                        new CryptoPrice(Instant.EPOCH, BigDecimal.ZERO),
                        new CryptoPrice(Instant.EPOCH, BigDecimal.TEN)
                )
        ));

        // When Then
        assertThat(repository.getMinPrice(BTC)).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    void shouldReturnMaxPrice() {
        // Given
        val repository = new CryptoPricesRepository(Map.of(
                BTC,
                List.of(
                        new CryptoPrice(Instant.EPOCH, BigDecimal.ZERO),
                        new CryptoPrice(Instant.EPOCH, BigDecimal.TEN),
                        new CryptoPrice(Instant.EPOCH, BigDecimal.ONE)
                )
        ));

        // When Then
        assertThat(repository.getMaxPrice(BTC)).isEqualTo(BigDecimal.TEN);
    }

    @Test
    void shouldThrowIllegalStateExceptionGivenUnknownCryptoForMinPrice() {
        // Given
        val repository = new CryptoPricesRepository(Map.of(BTC, List.of()));

        // When Then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> repository.getMinPrice(XRP))
                .withMessage("Such crypto currency is not supported");
    }

    @Test
    void shouldThrowIllegalStateExceptionGivenUnknownCryptoForMaxPrice() {
        // Given
        val repository = new CryptoPricesRepository(Map.of(BTC, List.of()));

        // When Then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> repository.getMaxPrice(XRP))
                .withMessage("Such crypto currency is not supported");
    }

    @Test
    void shouldThrowNoSuchElementExceptionGivenNoPricesForMinPrice() {
        // Given
        val repository = new CryptoPricesRepository(Map.of(BTC, List.of()));

        // When Then
        assertThatThrownBy(() -> repository.getMinPrice(BTC)).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void shouldThrowNoSuchElementExceptionGivenNoPricesForMaxPrice() {
        // Given
        val repository = new CryptoPricesRepository(Map.of(BTC, List.of()));

        // When Then
        assertThatThrownBy(() -> repository.getMaxPrice(BTC)).isInstanceOf(NoSuchElementException.class);
    }
}