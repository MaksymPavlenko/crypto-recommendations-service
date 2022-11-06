package com.crs.cryptorecommendationsservice.service;


import com.crs.cryptorecommendationsservice.repository.CryptoPricesRepository;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Set;

import static com.crs.cryptorecommendationsservice.TestUtils.createCryptoPrice;
import static org.assertj.core.api.Assertions.assertThat;

class RecommendationsServiceTest {

    private static final String BTC = "BTC", ETH = "ETH";
    private static final int SCALE = 4;

    private final CryptoPricesRepository repository = Mockito.mock(CryptoPricesRepository.class);

    private final RecommendationsService service = new RecommendationsService(repository, SCALE);

    @Test
    void shouldReturnNormalizedRangesInDecreasingOrder() {
        // Given
        Mockito.when(repository.getSymbols()).thenReturn(Set.of(BTC, ETH));
        Mockito.when(repository.getMinPrice(BTC)).thenReturn(createCryptoPrice(40000));
        Mockito.when(repository.getMaxPrice(BTC)).thenReturn(createCryptoPrice(50000));
        Mockito.when(repository.getMinPrice(ETH)).thenReturn(createCryptoPrice(3000));
        Mockito.when(repository.getMaxPrice(ETH)).thenReturn(createCryptoPrice(4000));

        // When
        val normalizedRanges = service.getNormalizedRanges();

        // Then
        assertThat(normalizedRanges.normalizedRanges().size()).isEqualTo(2);
        assertThat(normalizedRanges.normalizedRanges().get(0).symbol()).isEqualTo(ETH);
        assertThat(normalizedRanges.normalizedRanges().get(0).value()).isEqualTo(BigDecimal.valueOf(0.3333));
        assertThat(normalizedRanges.normalizedRanges().get(1).symbol()).isEqualTo(BTC);
        assertThat(normalizedRanges.normalizedRanges().get(1).value()).isEqualTo(BigDecimal.valueOf(0.25));
    }

    @Test
    void shouldReturnCryptocurrencyDetails() {
        // Given
        val oldestPrice = createCryptoPrice(50000, -86400);
        val newestPrice = createCryptoPrice(52000, +86400);
        val minPrice = createCryptoPrice(48000);
        val maxPrice = createCryptoPrice(55000);
        Mockito.when(repository.getOldestPrice(BTC)).thenReturn(oldestPrice);
        Mockito.when(repository.getNewestPrice(BTC)).thenReturn(newestPrice);
        Mockito.when(repository.getMinPrice(BTC)).thenReturn(minPrice);
        Mockito.when(repository.getMaxPrice(BTC)).thenReturn(maxPrice);

        // When
        val details = service.getCryptocurrencyDetails(BTC);

        // Then
        assertThat(details.symbol()).isEqualTo(BTC);
        assertThat(details.oldestPrice()).isEqualTo(oldestPrice);
        assertThat(details.newestPrice()).isEqualTo(newestPrice);
        assertThat(details.minPrice()).isEqualTo(minPrice);
        assertThat(details.maxPrice()).isEqualTo(maxPrice);
    }

}