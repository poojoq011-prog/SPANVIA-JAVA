package com.pooja.spanvia.util;

import com.pooja.spanvia.exception.InvalidDatasetException;
import com.pooja.spanvia.model.HeritageSite;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CSVDataLoaderTest {

    @Test
    @DisplayName("Test loading valid CSV dataset file")
    public void testLoadValidCSVDataset() {
        List<HeritageSite> sites = CSVDataLoader.loadSites("data/heritage_sites.csv");

        assertNotNull(sites);
        assertFalse(sites.isEmpty(), "Loaded sites list should not be empty");
        assertTrue(sites.size() >= 100, "Should load dataset containing over 100 heritage sites");

        HeritageSite firstSite = sites.get(0);
        assertNotNull(firstSite.getSiteName());
        assertNotNull(firstSite.getState());
    }

    @Test
    @DisplayName("Test InvalidDatasetException creation and message retrieval")
    public void testInvalidDatasetException() {
        InvalidDatasetException ex = new InvalidDatasetException("Dataset parsing failed");
        assertEquals("Dataset parsing failed", ex.getMessage());

        Throwable cause = new IllegalArgumentException("File empty");
        InvalidDatasetException exWithCause = new InvalidDatasetException("Dataset error", cause);
        assertEquals("Dataset error", exWithCause.getMessage());
        assertEquals(cause, exWithCause.getCause());
    }
}
