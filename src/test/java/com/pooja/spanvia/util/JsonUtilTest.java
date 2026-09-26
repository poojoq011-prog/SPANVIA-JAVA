package com.pooja.spanvia.util;

import com.pooja.spanvia.model.HeritageSite;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonUtilTest {

    @Test
    @DisplayName("Test escapeJson string escaping")
    public void testEscapeJson() {
        assertEquals("", JsonUtil.escapeJson(null));
        assertEquals("Hello \\\"World\\\"", JsonUtil.escapeJson("Hello \"World\""));
        assertEquals("Line1\\nLine2", JsonUtil.escapeJson("Line1\nLine2"));
    }

    @Test
    @DisplayName("Test toJson single site serialization")
    public void testToJsonSingleSite() {
        assertEquals("null", JsonUtil.toJson(null));

        HeritageSite site = new HeritageSite(
                1, "Hampi", "Karnataka", "Ruins", 5000.0, true,
                "Fort", "Winter", "Hampi Utsav", "Virupaksha", "Easy", 4.8
        );
        String json = JsonUtil.toJson(site);
        assertTrue(json.contains("\"id\":1"));
        assertTrue(json.contains("\"name\":\"Hampi\""));
        assertTrue(json.contains("\"unesco\":true"));
    }

    @Test
    @DisplayName("Test toJsonList collection serialization")
    public void testToJsonList() {
        assertEquals("[]", JsonUtil.toJsonList(null));
        assertEquals("[]", JsonUtil.toJsonList(Collections.emptyList()));

        HeritageSite site1 = new HeritageSite(1, "Site A", "State A", "Hist A", 1000.0, false, "Cat", "Time", "Fest", "Attr", "Easy", 4.5);
        HeritageSite site2 = new HeritageSite(2, "Site B", "State B", "Hist B", 2000.0, true, "Cat", "Time", "Fest", "Attr", "Easy", 4.6);
        List<HeritageSite> list = Arrays.asList(site1, site2);

        String jsonList = JsonUtil.toJsonList(list);
        assertTrue(jsonList.startsWith("[") && jsonList.endsWith("]"));
        assertTrue(jsonList.contains("Site A"));
        assertTrue(jsonList.contains("Site B"));
    }

    @Test
    @DisplayName("Test toStatsJson and toFestivalsJson output")
    public void testStatsAndFestivalsJson() {
        String statsJson = JsonUtil.toStatsJson(100, 40, 25);
        assertTrue(statsJson.contains("\"totalSites\":100"));

        String festivalsJson = JsonUtil.toFestivalsJson(new String[]{"Diwali", "Holi"}, new ArrayList<>());
        assertTrue(festivalsJson.contains("\"featuredFestivals\":[\"Diwali\",\"Holi\"]"));
    }
}
