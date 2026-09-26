package com.pooja.spanvia.service;

import com.pooja.spanvia.exception.InvalidDatasetException;
import com.pooja.spanvia.exception.SiteNotFoundException;
import com.pooja.spanvia.model.HeritageSite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SpanviaServiceTest {

    private SpanviaService service;

    @BeforeEach
    public void setUp() throws InvalidDatasetException {
        service = new SpanviaService();
        service.loadInitialData("data/heritage_sites.csv");
    }

    @Test
    @DisplayName("Test initial data loading into service")
    public void testInitialDataLoading() {
        List<HeritageSite> sites = service.getAllSitesList();
        assertNotNull(sites);
        assertFalse(sites.isEmpty());
        assertTrue(sites.size() >= 100);
    }

    @Test
    @DisplayName("Test getSiteById with valid ID")
    public void testGetSiteByIdSuccess() {
        HeritageSite site = service.getSiteById(1);
        assertNotNull(site);
        assertEquals(1, site.getSiteId());
        assertEquals("Brihadeeswarar Temple", site.getSiteName());
    }

    @Test
    @DisplayName("Test getSiteByIdOrThrow with invalid ID throws SiteNotFoundException")
    public void testGetSiteByIdOrThrowNotFound() {
        assertThrows(SiteNotFoundException.class, () -> {
            service.getSiteByIdOrThrow(-999);
        });
    }

    @Test
    @DisplayName("Test search by state matching and empty result handling")
    public void testSearchByState() {
        List<HeritageSite> tamilNaduSites = service.filterByState("Tamil Nadu");
        assertNotNull(tamilNaduSites);
        assertFalse(tamilNaduSites.isEmpty());

        List<HeritageSite> invalidStateSites = service.filterByState("NonExistentState123");
        assertNotNull(invalidStateSites);
        assertTrue(invalidStateSites.isEmpty());

        assertDoesNotThrow(() -> service.searchByState("Karnataka"));
        assertDoesNotThrow(() -> service.searchByState("UnknownState"));
    }

    @Test
    @DisplayName("Test search by keyword matching")
    public void testSearchByKeyword() {
        List<HeritageSite> fortSites = service.filterByKeyword("Fort");
        assertNotNull(fortSites);
        assertFalse(fortSites.isEmpty());

        List<HeritageSite> unknownKeywordSites = service.filterByKeyword("xyzxyzxyz");
        assertNotNull(unknownKeywordSites);
        assertTrue(unknownKeywordSites.isEmpty());

        assertDoesNotThrow(() -> service.searchByKeyword("Temple"));
        assertDoesNotThrow(() -> service.searchByKeyword("UnknownKeyword"));
    }

    @Test
    @DisplayName("Test filtering sites by budget")
    public void testFilterByBudget() {
        List<HeritageSite> budgetSites = service.filterByBudget(2000.0);
        assertNotNull(budgetSites);
        for (HeritageSite site : budgetSites) {
            assertTrue(site.getEstimatedBudget() <= 2000.0);
        }

        assertDoesNotThrow(() -> service.recommendByBudget(1500.0));
        assertDoesNotThrow(() -> service.recommendByBudget(10.0));
    }

    @Test
    @DisplayName("Test UNESCO sites extraction")
    public void testGetUnescoSites() {
        ArrayList<HeritageSite> unescoSites = service.getUnescoSites();
        assertNotNull(unescoSites);
        assertFalse(unescoSites.isEmpty());
        for (HeritageSite site : unescoSites) {
            assertTrue(site.isUnesco());
        }
    }

    @Test
    @DisplayName("Test adding custom site to service")
    public void testAddSite() {
        int initialCount = service.getAllSitesList().size();
        HeritageSite customSite = new HeritageSite(
                9999, "Custom Palace", "Rajasthan", "Historic palace",
                1200.0, false, "Palace", "October", "Fest", "Attraction", "Easy", 4.5
        );

        service.addSite(customSite);
        assertEquals(initialCount + 1, service.getAllSitesList().size());
        assertEquals("Custom Palace", service.getSiteById(9999).getSiteName());
    }

    @Test
    @DisplayName("Test AI Chat search logic")
    public void testChatAI() {
        String answerHampi = service.chatAI("Where is Hampi?", null);
        assertNotNull(answerHampi);
        assertTrue(answerHampi.toLowerCase().contains("karnataka"));

        String answerEmpty = service.chatAI("", null);
        assertNotNull(answerEmpty);
    }

    @Test
    @DisplayName("Test display methods")
    public void testDisplayMethods() {
        assertDoesNotThrow(service::displayAllSites);
        assertDoesNotThrow(service::displayFestivals);
        assertDoesNotThrow(service::showSiteCount);
        assertNotNull(service.getFeaturedFestivals());
    }
}
