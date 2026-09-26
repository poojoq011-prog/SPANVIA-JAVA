package com.pooja.spanvia.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HeritageSiteTest {

    @Test
    @DisplayName("Test HeritageSite creation and getters")
    public void testHeritageSiteCreationAndGetters() {
        HeritageSite site = new HeritageSite(
                101,
                "Taj Mahal",
                "Uttar Pradesh",
                "Mughal Architecture marvel built by Shah Jahan",
                2500.0,
                true,
                "Mausoleum",
                "October to March",
                "Taj Mahotsav",
                "Agra Fort",
                "Easy",
                4.9
        );

        assertEquals(101, site.getSiteId());
        assertEquals("Taj Mahal", site.getSiteName());
        assertEquals("Uttar Pradesh", site.getState());
        assertEquals("Mughal Architecture marvel built by Shah Jahan", site.getHistory());
        assertEquals(2500.0, site.getEstimatedBudget(), 0.001);
        assertTrue(site.isUnesco());
        assertEquals("Mausoleum", site.getCategory());
        assertEquals("October to March", site.getBestTravelTime());
        assertEquals("Taj Mahotsav", site.getFestival());
        assertEquals("Agra Fort", site.getNearbyAttraction());
        assertEquals("Easy", site.getAccessibility());
        assertEquals(4.9, site.getRating(), 0.001);
        assertEquals("Taj Mahal (Uttar Pradesh)", site.toString());
    }

    @Test
    @DisplayName("Test TempleSite creation and deity getter")
    public void testTempleSiteCreation() {
        TempleSite temple = new TempleSite(
                102,
                "Meenakshi Amman Temple",
                "Tamil Nadu",
                "Dravidian architecture marvel",
                1500.0,
                false,
                "Shiva & Parvati",
                "October to March",
                "Chithirai Festival",
                "Thirumalai Nayakkar Palace",
                "Easy",
                4.9
        );

        assertEquals(102, temple.getSiteId());
        assertEquals("Meenakshi Amman Temple", temple.getSiteName());
        assertEquals("Shiva & Parvati", temple.getDeity());
        assertEquals("Temple", temple.getCategory());
    }

    @Test
    @DisplayName("Test FortSite creation and architectural style getter")
    public void testFortSiteCreation() {
        FortSite fort = new FortSite(
                103,
                "Gwalior Fort",
                "Madhya Pradesh",
                "Hilltop fortress spanning centuries of Indian dynasties",
                2000.0,
                false,
                "Gwalior Hilltop Architecture",
                "October to March",
                "Tansen Music Festival",
                "Jai Vilas Palace",
                "Moderate",
                4.8
        );

        assertEquals(103, fort.getSiteId());
        assertEquals("Gwalior Fort", fort.getSiteName());
        assertEquals("Gwalior Hilltop Architecture", fort.getArchitecturalStyle());
        assertEquals("Fort", fort.getCategory());
    }

    @Test
    @DisplayName("Test site static counter tracking")
    public void testStaticSiteCounter() {
        int initialCounter = HeritageSite.getSiteCounter();
        new HeritageSite(
                999, "Test Site", "State", "History", 100.0, false,
                "Test", "Winter", "Fest", "Attraction", "Easy", 4.0
        );
        assertEquals(initialCounter + 1, HeritageSite.getSiteCounter());
    }

    @Test
    @DisplayName("Test display and interface methods execution")
    public void testDisplayAndInterfaceMethods() {
        HeritageSite site = new HeritageSite(
                104, "Hampi Ruins", "Karnataka", "Vijayanagara Empire",
                3000.0, true, "Fort Ruins", "Winter", "Hampi Utsav",
                "Virupaksha Temple", "Easy", 4.8
        );

        assertDoesNotThrow(site::showCategory);
        assertDoesNotThrow(site::showTravelTips);
        assertDoesNotThrow(site::showNearbyAttractions);
        assertDoesNotThrow(site::displaySite);
    }
}
