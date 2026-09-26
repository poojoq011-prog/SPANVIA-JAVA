package com.pooja.spanvia.util;

import com.pooja.spanvia.exception.InvalidDatasetException;
import com.pooja.spanvia.model.FortSite;
import com.pooja.spanvia.model.HeritageSite;
import com.pooja.spanvia.model.TempleSite;

import java.io.*;
import java.util.ArrayList;

/**
 * Utility class for loading heritage sites from CSV dataset using Java File I/O.
 */
public class CSVDataLoader {

    public static ArrayList<HeritageSite> loadSites(String path) {
        ArrayList<HeritageSite> sites = new ArrayList<>();

        File file = new File(path);
        if (!file.exists()) {
            String[] candidatePaths = {
                "data/heritage_sites.csv",
                "../data/heritage_sites.csv",
                "./data/heritage_sites.csv",
                "c:/Users/POOJA/spanvia-backend/data/heritage_sites.csv"
            };
            for (String cp : candidatePaths) {
                File candidate = new File(cp);
                if (candidate.exists()) {
                    file = candidate;
                    break;
                }
            }
        }

        if (!file.exists()) {
            System.err.println("Dataset file not found at path: " + path);
            return sites;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            // Skip header line
            br.readLine();

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                try {
                    String[] data = line.split(",");
                    if (data.length < 12) {
                        continue;
                    }

                    int id = Integer.parseInt(data[0].trim());
                    String name = data[1].trim();
                    String state = data[2].trim();
                    String category = data[3].trim();
                    String history = data[4].trim();
                    String bestTime = data[5].trim();
                    double budget = Double.parseDouble(data[6].trim());
                    boolean unesco = Boolean.parseBoolean(data[7].trim());
                    String festival = data[8].trim();
                    String nearbyAttraction = data[9].trim();
                    String accessibility = data[10].trim();
                    double rating = Double.parseDouble(data[11].trim());

                    HeritageSite site;
                    if ("Temple".equalsIgnoreCase(category)) {
                        site = new TempleSite(id, name, state, history, budget, unesco, "Dravidian/Nagara Architecture", bestTime, festival, nearbyAttraction, accessibility, rating);
                    } else if ("Fort".equalsIgnoreCase(category)) {
                        site = new FortSite(id, name, state, history, budget, unesco, "Indo-Islamic & Rajput Architecture", bestTime, festival, nearbyAttraction, accessibility, rating);
                    } else {
                        site = new HeritageSite(id, name, state, history, budget, unesco, category, bestTime, festival, nearbyAttraction, accessibility, rating);
                    }

                    sites.add(site);
                } catch (Exception ex) {
                    System.out.println("Skipping malformed CSV row: " + line);
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading dataset file: " + e.getMessage());
        }

        return sites;
    }

    public static ArrayList<HeritageSite> loadSitesOrThrow(String path) throws InvalidDatasetException {
        ArrayList<HeritageSite> list = loadSites(path);
        if (list.isEmpty()) {
            throw new InvalidDatasetException("Failed to load heritage dataset from path: " + path);
        }
        return list;
    }
}