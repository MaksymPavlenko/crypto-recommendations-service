package com.crs.cryptorecommendationsservice.service;


import com.crs.cryptorecommendationsservice.repository.CryptoPricesRepository;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Set;

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
        Mockito.when(repository.getMinPrice(BTC)).thenReturn(BigDecimal.valueOf(40000));
        Mockito.when(repository.getMaxPrice(BTC)).thenReturn(BigDecimal.valueOf(50000));
        Mockito.when(repository.getMinPrice(ETH)).thenReturn(BigDecimal.valueOf(3000));
        Mockito.when(repository.getMaxPrice(ETH)).thenReturn(BigDecimal.valueOf(4000));

        // When
        val normalizedRanges = service.getNormalizedRanges();

        // Then
        assertThat(normalizedRanges.normalizedRanges().size()).isEqualTo(2);
        assertThat(normalizedRanges.normalizedRanges().get(0).symbol()).isEqualTo(ETH);
        assertThat(normalizedRanges.normalizedRanges().get(0).value()).isEqualTo(BigDecimal.valueOf(0.3333));
        assertThat(normalizedRanges.normalizedRanges().get(1).symbol()).isEqualTo(BTC);
        assertThat(normalizedRanges.normalizedRanges().get(1).value()).isEqualTo(BigDecimal.valueOf(0.25));
    }

}