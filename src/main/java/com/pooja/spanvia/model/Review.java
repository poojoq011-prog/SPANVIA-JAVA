package com.pooja.spanvia.model;

/**
 * Review class with an inner class.
 */
public class Review {

    private String userName;
    private int rating;
    private String comment;

    public Review(String userName,
                  int rating,
                  String comment) {

        this.userName = userName;
        this.rating = rating;
        this.comment = comment;
    }

    public void displayReview() {

        System.out.println(
            userName + " rated " + rating + "/5");
        System.out.println(comment);
    }

    // Inner class
    public class CommentAnalyzer {

        public int wordCount() {
            return comment.split("\\s+").length;
        }
    }
}