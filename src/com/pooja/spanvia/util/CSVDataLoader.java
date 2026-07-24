package com.pooja.spanvia.util;

import com.pooja.spanvia.model.HeritageSite;

import java.io.*;
import java.util.ArrayList;

public class CSVDataLoader {

    public static ArrayList<HeritageSite> loadSites(String path) {

        ArrayList<HeritageSite> sites = new ArrayList<>();

        try {
            BufferedReader br =
                new BufferedReader(new FileReader(path));

            String line;

            // Skip header
            br.readLine();

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                HeritageSite site = new HeritageSite(
                        Integer.parseInt(data[0]),   // id
                        data[1],                     // name
                        data[2],                     // state
                        data[4],                     // history
                        Double.parseDouble(data[6]), // budget
                        Boolean.parseBoolean(data[7]), // unesco
                        data[3],                     // category
                        data[5],                     // best time
                        data[8],                     // festival
                        data[9],                     // nearby attraction
                        data[10],                    // accessibility
                        Double.parseDouble(data[11]) // rating
                );

                sites.add(site);
            }

            br.close();

        } catch (Exception e) {
            System.out.println(
                "Error loading dataset: " + e.getMessage());
        }

        return sites;
    }
}