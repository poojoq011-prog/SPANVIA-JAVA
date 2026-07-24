package com.pooja.spanvia.model;

/**
 * Abstract superclass for all cultural places.
 */
public abstract class CulturalPlace {

    // Protected member
    protected String category;

    // Constructor
    public CulturalPlace(String category) {
        this.category = category;
    }

    // Abstract method
    public abstract void showCategory();

    // Normal method
    public String getCategory() {
        return category;
    }
}