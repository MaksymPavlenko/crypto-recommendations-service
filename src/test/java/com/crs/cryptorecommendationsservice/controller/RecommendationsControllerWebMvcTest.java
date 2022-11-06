package com.crs.cryptorecommendationsservice.controller;

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

}
