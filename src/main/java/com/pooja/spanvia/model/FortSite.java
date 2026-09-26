package com.pooja.spanvia.model;

/**
 * FortSite is a specialized subclass of HeritageSite representing defensive fortifications.
 * Demonstrates OOP Inheritance and Polymorphic method overriding.
 */
public class FortSite extends HeritageSite {

    private String architecturalStyle;

    public FortSite(int siteId, String siteName,
                    String state, String history,
                    double estimatedBudget,
                    boolean unesco,
                    String architecturalStyle,
                    String bestTime,
                    String festival,
                    String nearbyAttraction,
                    String accessibility,
                    double rating) {

        super(siteId, siteName, state, history,
              estimatedBudget, unesco, "Fort",
              bestTime, festival, nearbyAttraction, accessibility, rating);

        this.architecturalStyle = architecturalStyle;
    }

    public String getArchitecturalStyle() {
        return architecturalStyle;
    }

    @Override
    public void showCategory() {
        System.out.println("Fort Heritage Site - Architecture: " + architecturalStyle);
    }
}
