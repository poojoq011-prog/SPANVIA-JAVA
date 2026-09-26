package com.pooja.spanvia.model;

/**
 * TempleSite is a specialized subclass of HeritageSite demonstrating OOP Inheritance and Polymorphism.
 */
public class TempleSite extends HeritageSite {

    private String deity;

    public TempleSite(int siteId, String siteName,
                      String state, String history,
                      double estimatedBudget,
                      boolean unesco,
                      String deity,
                      String bestTime,
                      String festival,
                      String nearbyAttraction,
                      String accessibility,
                      double rating) {

        super(siteId, siteName, state, history,
              estimatedBudget, unesco, "Temple",
              bestTime, festival, nearbyAttraction, accessibility, rating);

        this.deity = deity;
    }

    public String getDeity() {
        return deity;
    }

    @Override
    public void showCategory() {
        System.out.println("Temple Heritage Site - Dedicated Deity: " + (deity != null ? deity : "Ancient Tradition"));
    }
}