package com.pooja.spanvia.service;
import com.pooja.spanvia.util.CSVDataLoader;

import com.pooja.spanvia.model.HeritageSite;

import java.util.ArrayList;

/**
 * Service class for managing heritage sites.
 */
public class SpanviaService {

    // ArrayList concept
    private ArrayList<HeritageSite> sites =
            new ArrayList<>();

    // Array concept
    private String[] featuredFestivals = {
        "Pongal",
        "Navaratri",
        "Karthigai Deepam",
        "Hampi Utsav"
    };

    // Add heritage site
    public void addSite(HeritageSite site) {
        sites.add(site);
        System.out.println(site.getSiteName() +
                " added successfully!");
    }
    public void loadInitialData(String csvPath) {

    sites.addAll(
        CSVDataLoader.loadSites(csvPath));

    System.out.println(
        sites.size() + " heritage sites loaded from dataset.");
}

    // Display all festivals (for loop)
    public void displayFestivals() {

        System.out.println(
            "\n===== Featured Festivals =====");

        for (String festival : featuredFestivals) {
            System.out.println("- " + festival);
        }
    }

    // Display all sites
    public void displayAllSites() {

        if (sites.isEmpty()) {
            System.out.println(
                "No heritage sites available.");
            return;
        }

        for (HeritageSite site : sites) {
            site.displaySite();
        }
    }

    // Search by state
    public void searchByState(String state) {

        boolean found = false;

        for (HeritageSite site : sites) {

            // String method
            if (site.getState()
                    .equalsIgnoreCase(state)) {

                site.displaySite();
                found = true;
            }
        }

        // if-else concept
        if (!found) {
            System.out.println(
                "No heritage sites found in " + state);
        } else {
            System.out.println(
                "Search completed successfully.");
        }
    }

    // Search by keyword
    public void searchByKeyword(String keyword) {

        boolean found = false;

        for (HeritageSite site : sites) {

            if (site.getSiteName()
                    .toLowerCase()
                    .contains(keyword.toLowerCase())) {

                site.displaySite();
                found = true;
            }
        }

        if (!found) {
            System.out.println(
                "No sites found with keyword: " + keyword);
        }
    }

    // Budget recommendation
    public void recommendByBudget(double maxBudget) {

        boolean found = false;

        for (HeritageSite site : sites) {

            // Operator usage
            if (site.getEstimatedBudget() <= maxBudget) {

                // StringBuilder usage
                StringBuilder builder =
                        new StringBuilder();

                builder.append("Recommended: ")
                       .append(site.getSiteName())
                       .append(" within ₹")
                       .append(maxBudget);

                System.out.println(builder);
                found = true;
            }
        }

        if (!found) {
            System.out.println(
                "No sites available within this budget.");
        }
    }

    // while loop example
    public void showSiteCount() {

        int count = 0;
        int index = 0;

        while (index < sites.size()) {
            count++;
            index++;
        }

        System.out.println(
            "Total heritage sites stored: " + count);
    }
}