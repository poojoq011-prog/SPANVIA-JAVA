package com.pooja.spanvia.model;

import com.pooja.spanvia.interfaces.AdvancedTravelable;

public class HeritageSite extends CulturalPlace
        implements AdvancedTravelable {

    // Static variable
    private static int siteCounter = 0;

    // Encapsulated variables
    private int siteId;
    private String siteName;
    private String state;
    private String history;
    private double estimatedBudget;
    private boolean unesco;

    // Additional variables
    private String bestTime;
    private String festival;
    private String nearbyAttraction;
    private String accessibility;
    private double rating;

    // Constructor
    public HeritageSite(int siteId, String siteName,
                        String state, String history,
                        double estimatedBudget,
                        boolean unesco,
                        String category,
                        String bestTime,
                        String festival,
                        String nearbyAttraction,
                        String accessibility,
                        double rating) {

        super(category);

        this.siteId = siteId;
        this.siteName = siteName;
        this.state = state;
        this.history = history;
        this.estimatedBudget = estimatedBudget;
        this.unesco = unesco;
        this.bestTime = bestTime;
        this.festival = festival;
        this.nearbyAttraction = nearbyAttraction;
        this.accessibility = accessibility;
        this.rating = rating;

        siteCounter++;
    }

    // Getter methods
    public int getSiteId() {
        return siteId;
    }

    public String getSiteName() {
        return siteName;
    }

    public String getState() {
        return state;
    }

    public double getEstimatedBudget() {
        return estimatedBudget;
    }

    public String getHistory() {
        return history;
    }

    public boolean isUnesco() {
        return unesco;
    }

    public String getFestival() {
        return festival;
    }

    public String getNearbyAttraction() {
        return nearbyAttraction;
    }

    public String getAccessibility() {
        return accessibility;
    }

    public double getRating() {
        return rating;
    }

    // Override abstract method
    @Override
    public void showCategory() {
        System.out.println("Category: " + category);
    }

    // Interface methods
    @Override
    public String getBestTravelTime() {
        return bestTime;
    }

    @Override
    public void showTravelTips() {
        System.out.println(
            "Carry water and wear comfortable footwear.");
    }

    @Override
    public void showNearbyAttractions() {
        System.out.println(
            "Nearby Attraction: " + nearbyAttraction);
    }

    // Display method
    public void displaySite() {

        StringBuffer buffer = new StringBuffer();

        buffer.append("\n===== Heritage Site =====\n");
        buffer.append("ID: ").append(siteId).append("\n");
        buffer.append("Name: ").append(siteName).append("\n");
        buffer.append("State: ").append(state).append("\n");
        buffer.append("Category: ").append(category).append("\n");
        buffer.append("History: ").append(history).append("\n");
        buffer.append("Best Time: ").append(bestTime).append("\n");
        buffer.append("Festival: ").append(festival).append("\n");
        buffer.append("Nearby Attraction: ")
              .append(nearbyAttraction).append("\n");
        buffer.append("Accessibility: ")
              .append(accessibility).append("\n");
        buffer.append("Visitor Rating: ")
              .append(rating).append("\n");
        buffer.append("Budget: ₹")
              .append(estimatedBudget).append("\n");
        buffer.append("UNESCO: ")
              .append(unesco ? "Yes" : "No");

        System.out.println(buffer);
    }

    // Static method
    public static int getSiteCounter() {
        return siteCounter;
    }

    // Object class method
    @Override
    public String toString() {
        return siteName + " (" + state + ")";
    }
}