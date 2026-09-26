package com.pooja.spanvia.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SpanviaRulesTest {

    @Test
    @DisplayName("Test SpanviaRules constant and showRules method")
    public void testSpanviaRules() {
        assertEquals(200, SpanviaRules.MAX_REVIEW_LENGTH);
        assertDoesNotThrow(SpanviaRules::showRules);
    }
}
