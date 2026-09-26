package com.pooja.spanvia.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ReviewTest {

    @Test
    @DisplayName("Test Review instantiation and methods")
    public void testReviewCreation() {
        Review review = new Review("Aarav", 5, "Breathtaking monument and great experience!");

        assertDoesNotThrow(review::displayReview);
    }

    @Test
    @DisplayName("Test Review inner class CommentAnalyzer word count")
    public void testCommentAnalyzer() {
        Review positiveReview = new Review("Neha", 5, "Amazing and majestic heritage site!");
        Review.CommentAnalyzer analyzer = positiveReview.new CommentAnalyzer();

        int wordCount = analyzer.wordCount();
        assertEquals(5, wordCount);
    }
}
