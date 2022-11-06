package com.crs.cryptorecommendationsservice.controller;

import com.crs.cryptorecommendationsservice.exception.NoPricesFoundException;
import com.crs.cryptorecommendationsservice.exception.UnsupportedCryptocurrencyException;
import com.crs.cryptorecommendationsservice.model.CryptocurrencyDetails;
import com.crs.cryptorecommendationsservice.model.NormalizedRange;
import com.crs.cryptorecommendationsservice.model.NormalizedRanges;
import com.crs.cryptorecommendationsservice.service.RecommendationsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static com.crs.cryptorecommendationsservice.TestUtils.createCryptoPrice;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecommendationsController.class)
class RecommendationsControllerWebMvcTest {

    private static final String BTC = "BTC", ETH = "ETH", XRP = "XRP";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RecommendationsService service;

    @Test
    void shouldReturnNormalizedRanges() throws Exception {
        // Given
        Mockito.when(service.getNormalizedRanges()).thenReturn(new NormalizedRanges(List.of(
                new NormalizedRange(ETH, BigDecimal.valueOf(0.6)),
                new NormalizedRange(XRP, BigDecimal.valueOf(0.5)),
                new NormalizedRange(BTC, BigDecimal.valueOf(0.4))
        )));

        // When Then
        mockMvc.perform(get("/v1/normalizedRanges").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.normalizedRanges[0].symbol").value(ETH))
                .andExpect(jsonPath("$.normalizedRanges[0].value").value(0.6))
                .andExpect(jsonPath("$.normalizedRanges[1].symbol").value(XRP))
                .andExpect(jsonPath("$.normalizedRanges[1].value").value(0.5))
                .andExpect(jsonPath("$.normalizedRanges[2].symbol").value(BTC))
                .andExpect(jsonPath("$.normalizedRanges[2].value").value(0.4));
    }

    @Test
    void shouldReturnCryptocurrencyDetails() throws Exception {
        // Given
        Mockito.when(service.getCryptocurrencyDetails(BTC))
                .thenReturn(new CryptocurrencyDetails(
                        BTC,
                        createCryptoPrice(50000, "2022-11-01T10:15:30Z"),
                        createCryptoPrice(52000, "2022-11-06T10:15:30Z"),
                        createCryptoPrice(48000, "2022-11-03T10:15:30Z"),
                        createCryptoPrice(55000, "2022-11-04T10:15:30Z")
                ));

        // When Then
        mockMvc.perform(get("/v1/crypto/BTC").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.symbol").value(BTC))
                .andExpect(jsonPath("$.oldestPrice.timestamp").value("2022-11-01T10:15:30Z"))
                .andExpect(jsonPath("$.oldestPrice.value").value(50000))
                .andExpect(jsonPath("$.newestPrice.timestamp").value("2022-11-06T10:15:30Z"))
                .andExpect(jsonPath("$.newestPrice.value").value(52000))
                .andExpect(jsonPath("$.minPrice.timestamp").value("2022-11-03T10:15:30Z"))
                .andExpect(jsonPath("$.minPrice.value").value(48000))
                .andExpect(jsonPath("$.maxPrice.timestamp").value("2022-11-04T10:15:30Z"))
                .andExpect(jsonPath("$.maxPrice.value").value(55000));
    }

    @Test
    void shouldReturnErrorResponseGivenUnhandledExceptionWasThrown() throws Exception {
        // Given
        Mockito.when(service.getCryptocurrencyDetails(BTC)).thenThrow(new RuntimeException("Random exception"));

        // When Then
        mockMvc.perform(get("/v1/crypto/BTC").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value("500 Internal Server Error"))
                .andExpect(jsonPath("$.message").value("Internal server error"));
    }

    @Test
    void shouldReturnErrorResponseGivenUnsupportedCryptocurrencyExceptionWasThrown() throws Exception {
        // Given
        Mockito.when(service.getCryptocurrencyDetails(BTC)).thenThrow(new UnsupportedCryptocurrencyException(BTC));

        // When Then
        mockMvc.perform(get("/v1/crypto/BTC").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400 Bad Request"))
                .andExpect(jsonPath("$.message").value("The " + BTC + " cryptocurrency is not supported"));
    }

    @Test
    void shouldReturnErrorResponseGivenNoPricesFoundExceptionWasThrown() throws Exception {
        // Given
        Mockito.when(service.getCryptocurrencyDetails(BTC)).thenThrow(new NoPricesFoundException(BTC));

        // When Then
        mockMvc.perform(get("/v1/crypto/BTC").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value("500 Internal Server Error"))
                .andExpect(jsonPath("$.message").value("The prices data for " + BTC + " was not found"));
    }
}
