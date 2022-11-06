package com.crs.cryptorecommendationsservice.config;

import com.crs.cryptorecommendationsservice.exception.CsvReaderException;
import com.crs.cryptorecommendationsservice.model.CryptoPrice;
import com.crs.cryptorecommendationsservice.repository.CryptoPricesRepository;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Configuration
public class CryptoPricesRepositoryConfiguration {

    private final String pricesLocation;

    public CryptoPricesRepositoryConfiguration(@Value("${crypto.pricesLocation}") final String pricesLocation) {
        this.pricesLocation = pricesLocation;
    }

    @Bean
    public CryptoPricesRepository cryptoPricesRepository(
            @Value("${crypto.supported}") final Set<String> supportedCryptos
    ) throws CsvReaderException {
        return new CryptoPricesRepository(getCryptoPrices(supportedCryptos));
    }

    private Map<String, List<CryptoPrice>> getCryptoPrices(Set<String> supportedCryptos) {
        return supportedCryptos.stream()
                .map(this::getPricesFor)
                .collect(Collectors.toUnmodifiableMap(CryptoPrices::crypto, CryptoPrices::prices));
    }

    private CryptoPrices getPricesFor(String crypto) {
        val reader = new CsvPricesReader(getPriceLocation(pricesLocation, crypto), crypto);
        return new CryptoPrices(crypto.toUpperCase(), reader.getPrices());
    }

    private String getPriceLocation(final String pricesLocation, final String crypto) {
        return pricesLocation + "/" + crypto + "_values.csv";
    }

    private record CryptoPrices(String crypto, List<CryptoPrice> prices) {
    }
}

/**
 * CSV file reader to load prices from the classpath resources
 */
final class CsvPricesReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsvPricesReader.class);

    private static final String TIMESTAMP_COLUMN = "timestamp";
    private static final String SYMBOL_COLUMN = "symbol";
    private static final String PRICE_COLUMN = "price";
    private final Resource pricesFile;
    private final String crypto;
    private ColumnIndexes indexes;

    public CsvPricesReader(final String fileName, final String crypto) {
        pricesFile = new ClassPathResource(fileName);
        this.crypto = crypto;
    }

    /**
     * @return an immutable sorted list of prices for the specified cryptocurrency obtained from the specified CSV file
     * @throws CsvReaderException if the resource doesn't exist or cannot be read
     */
    public List<CryptoPrice> getPrices() throws CsvReaderException {
        try (InputStream inputStream = pricesFile.getInputStream();
             InputStreamReader isReader = new InputStreamReader(inputStream);
             BufferedReader reader = new BufferedReader(isReader)
        ) {
            val header = reader.readLine();
            indexes = getColumnIndexes(header);
            return reader.lines()
                    .filter(line -> getSymbol(line).equalsIgnoreCase(crypto))
                    .map(line -> new CryptoPrice(getTimestamp(line), getPrice(line)))
                    .sorted(Comparator.comparing(CryptoPrice::timestamp))
                    .toList();
        } catch (IOException e) {
            val message = "The resource " + pricesFile + " doesn't exist or cannot be read";
            LOGGER.error(message, e);
            throw new CsvReaderException(message, e);
        }
    }

    private ColumnIndexes getColumnIndexes(String header) {
        val columns = Arrays.asList(header.split(","));
        return new ColumnIndexes(
                getValidatedIndex(columns, TIMESTAMP_COLUMN),
                getValidatedIndex(columns, SYMBOL_COLUMN),
                getValidatedIndex(columns, PRICE_COLUMN)
        );
    }

    private int getValidatedIndex(final List<String> columns, final String columnName) {
        if (columns.contains(columnName)) {
            return columns.indexOf(columnName);
        } else {
            throw new CsvReaderException("Column's name doesn't match CSV file content");
        }
    }

    private String getSymbol(String line) {
        return line.split(",")[indexes.symbolIndex];
    }

    private Instant getTimestamp(String line) {
        val timestamp = line.split(",")[indexes.timestampIndex];
        return Instant.ofEpochMilli(Long.parseLong(timestamp));
    }

    private BigDecimal getPrice(String line) {
        val price = line.split(",")[indexes.priceIndex];
        return BigDecimal.valueOf(Double.parseDouble(price));
    }

    private record ColumnIndexes(int timestampIndex, int symbolIndex, int priceIndex) {
    }
}
