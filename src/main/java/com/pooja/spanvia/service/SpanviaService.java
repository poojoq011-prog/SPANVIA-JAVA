package com.pooja.spanvia.service;
import com.pooja.spanvia.exception.SiteNotFoundException;
import com.pooja.spanvia.util.CSVDataLoader;

import com.pooja.spanvia.model.HeritageSite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Service class for managing heritage sites using Core Java Collections and algorithms.
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

    // Methods for REST API
    public ArrayList<HeritageSite> getAllSitesList() {
        return sites;
    }

    public HeritageSite getSiteById(int id) {
        for (HeritageSite site : sites) {
            if (site.getSiteId() == id) {
                return site;
            }
        }
        return null;
    }

    public HeritageSite getSiteByIdOrThrow(int id) throws SiteNotFoundException {
        HeritageSite site = getSiteById(id);
        if (site == null) {
            throw new SiteNotFoundException(id);
        }
        return site;
    }

    public ArrayList<HeritageSite> filterAndSortSites(String keyword, String state, String category, double maxBudget, double minRating, Boolean unescoOnly, String accessibility, String sortBy) {
        ArrayList<HeritageSite> result = new ArrayList<>();

        for (HeritageSite site : sites) {
            boolean matches = true;

            // Keyword Search (Name, State, Category, History, Festival)
            if (keyword != null && !keyword.trim().isEmpty()) {
                String kw = keyword.toLowerCase().trim();
                boolean matchName = site.getSiteName() != null && site.getSiteName().toLowerCase().contains(kw);
                boolean matchState = site.getState() != null && site.getState().toLowerCase().contains(kw);
                boolean matchCategory = site.getCategory() != null && site.getCategory().toLowerCase().contains(kw);
                boolean matchHistory = site.getHistory() != null && site.getHistory().toLowerCase().contains(kw);
                boolean matchFestival = site.getFestival() != null && site.getFestival().toLowerCase().contains(kw);

                if (!matchName && !matchState && !matchCategory && !matchHistory && !matchFestival) {
                    matches = false;
                }
            }

            // State Filter
            if (matches && state != null && !state.trim().isEmpty() && !"All".equalsIgnoreCase(state.trim())) {
                if (!site.getState().equalsIgnoreCase(state.trim())) {
                    matches = false;
                }
            }

            // Category Filter
            if (matches && category != null && !category.trim().isEmpty() && !"All".equalsIgnoreCase(category.trim())) {
                if (!site.getCategory().equalsIgnoreCase(category.trim())) {
                    matches = false;
                }
            }

            // Budget Filter
            if (matches && maxBudget > 0 && site.getEstimatedBudget() > maxBudget) {
                matches = false;
            }

            // Rating Filter
            if (matches && minRating > 0 && site.getRating() < minRating) {
                matches = false;
            }

            // UNESCO Filter
            if (matches && unescoOnly != null && unescoOnly) {
                if (!site.isUnesco()) {
                    matches = false;
                }
            }

            // Accessibility Filter
            if (matches && accessibility != null && !accessibility.trim().isEmpty() && !"All".equalsIgnoreCase(accessibility.trim())) {
                if (site.getAccessibility() == null || !site.getAccessibility().toLowerCase().contains(accessibility.toLowerCase().trim())) {
                    matches = false;
                }
            }

            if (matches) {
                result.add(site);
            }
        }

        // Java Sorting using Comparator
        if (sortBy != null && !sortBy.trim().isEmpty()) {
            final String sortKey = sortBy.trim();
            Collections.sort(result, new Comparator<HeritageSite>() {
                @Override
                public int compare(HeritageSite a, HeritageSite b) {
                    if ("budget-asc".equalsIgnoreCase(sortKey)) {
                        return Double.compare(a.getEstimatedBudget(), b.getEstimatedBudget());
                    } else if ("budget-desc".equalsIgnoreCase(sortKey)) {
                        return Double.compare(b.getEstimatedBudget(), a.getEstimatedBudget());
                    } else if ("name-asc".equalsIgnoreCase(sortKey)) {
                        return a.getSiteName().compareToIgnoreCase(b.getSiteName());
                    } else { // default: rating-desc
                        return Double.compare(b.getRating(), a.getRating());
                    }
                }
            });
        }

        return result;
    }


    public ArrayList<HeritageSite> filterByState(String state) {
        ArrayList<HeritageSite> result = new ArrayList<>();
        if (state == null || state.trim().isEmpty()) {
            return sites;
        }
        for (HeritageSite site : sites) {
            if (site.getState().equalsIgnoreCase(state.trim())) {
                result.add(site);
            }
        }
        return result;
    }

    public ArrayList<HeritageSite> filterByKeyword(String keyword) {
        ArrayList<HeritageSite> result = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) {
            return sites;
        }
        String kw = keyword.toLowerCase().trim();
        for (HeritageSite site : sites) {
            if (site.getSiteName().toLowerCase().contains(kw) ||
                site.getState().toLowerCase().contains(kw) ||
                site.getCategory().toLowerCase().contains(kw) ||
                (site.getHistory() != null && site.getHistory().toLowerCase().contains(kw)) ||
                (site.getFestival() != null && site.getFestival().toLowerCase().contains(kw))) {
                result.add(site);
            }
        }
        return result;
    }

    public ArrayList<HeritageSite> filterByBudget(double maxBudget) {
        ArrayList<HeritageSite> result = new ArrayList<>();
        for (HeritageSite site : sites) {
            if (site.getEstimatedBudget() <= maxBudget) {
                result.add(site);
            }
        }
        return result;
    }

    public ArrayList<HeritageSite> getUnescoSites() {
        ArrayList<HeritageSite> result = new ArrayList<>();
        for (HeritageSite site : sites) {
            if (site.isUnesco()) {
                result.add(site);
            }
        }
        return result;
    }

    public String[] getFeaturedFestivals() {
        return featuredFestivals;
    }

    public ArrayList<HeritageSite> getSitesWithFestivals() {
        ArrayList<HeritageSite> result = new ArrayList<>();
        for (HeritageSite site : sites) {
            if (site.getFestival() != null && !site.getFestival().isEmpty() && !site.getFestival().equalsIgnoreCase("none")) {
                result.add(site);
            }
        }
        return result;
    }

    public ArrayList<HeritageSite> recommendAI(String state, String category, double maxBudget, double minRating, String accessibility, String travelSeason, String festival, Boolean unesco) {
        ArrayList<HeritageSite> result = new ArrayList<>();
        for (HeritageSite site : sites) {
            boolean matches = true;

            if (state != null && !state.trim().isEmpty() && !state.equalsIgnoreCase("All")) {
                if (!site.getState().equalsIgnoreCase(state.trim())) {
                    matches = false;
                }
            }

            if (category != null && !category.trim().isEmpty() && !category.equalsIgnoreCase("All")) {
                if (!site.getCategory().equalsIgnoreCase(category.trim())) {
                    matches = false;
                }
            }

            if (maxBudget > 0 && site.getEstimatedBudget() > maxBudget) {
                matches = false;
            }

            if (minRating > 0 && site.getRating() < minRating) {
                matches = false;
            }

            if (accessibility != null && !accessibility.trim().isEmpty() && !accessibility.equalsIgnoreCase("All")) {
                if (!site.getAccessibility().toLowerCase().contains(accessibility.toLowerCase().trim())) {
                    matches = false;
                }
            }

            if (travelSeason != null && !travelSeason.trim().isEmpty() && !travelSeason.equalsIgnoreCase("All")) {
                if (site.getBestTravelTime() != null && !site.getBestTravelTime().toLowerCase().contains(travelSeason.toLowerCase().trim())) {
                    matches = false;
                }
            }

            if (festival != null && !festival.trim().isEmpty() && !festival.equalsIgnoreCase("All")) {
                if (site.getFestival() != null && !site.getFestival().toLowerCase().contains(festival.toLowerCase().trim())) {
                    matches = false;
                }
            }

            if (unesco != null && unesco) {
                if (!site.isUnesco()) {
                    matches = false;
                }
            }

            if (matches) {
                result.add(site);
            }
        }

        // If strict filtering produces empty result, fall back to top rated matching budget
        if (result.isEmpty()) {
            for (HeritageSite site : sites) {
                if ((maxBudget <= 0 || site.getEstimatedBudget() <= maxBudget) && site.getRating() >= 4.5) {
                    result.add(site);
                }
            }
        }

        return result;
    }

    public String chatAI(String userQuestion, List<Map<String, String>> history) {
        if (userQuestion == null || userQuestion.trim().isEmpty()) {
            return "Please ask a question about Indian heritage, travel, or historical sites!";
        }

        String qLower = userQuestion.toLowerCase().trim();

        // 1. Resolve context site from history if question uses pronouns or implicit references
        HeritageSite matchedSite = null;

        // Check if user question explicitly mentions any site from SPANVIA dataset
        for (HeritageSite site : sites) {
            String nameLower = site.getSiteName().toLowerCase();
            if (qLower.contains(nameLower) || (nameLower.length() > 4 && qLower.contains(nameLower.split(" ")[0]))) {
                matchedSite = site;
                break;
            }
        }

        // If no explicit site in query, look through history for recent context
        if (matchedSite == null && history != null && !history.isEmpty()) {
            for (int i = history.size() - 1; i >= 0; i--) {
                Map<String, String> msg = history.get(i);
                String content = msg.getOrDefault("content", "").toLowerCase();
                for (HeritageSite site : sites) {
                    if (content.contains(site.getSiteName().toLowerCase())) {
                        matchedSite = site;
                        break;
                    }
                }
                if (matchedSite != null) break;
            }
        }

        // Check for specific question types about the matched site
        if (matchedSite != null) {
            // Location query
            if (qLower.contains("where") || qLower.contains("located") || qLower.contains("situation") || qLower.contains("address") || qLower.contains("find")) {
                StringBuilder sb = new StringBuilder();
                sb.append("**").append(matchedSite.getSiteName()).append("** is located in **").append(matchedSite.getState()).append("**, India.");
                if (matchedSite.getNearbyAttraction() != null && !matchedSite.getNearbyAttraction().isEmpty() && !matchedSite.getNearbyAttraction().equalsIgnoreCase("none")) {
                    sb.append("\n\n📍 **Nearby Landmark:** ").append(matchedSite.getNearbyAttraction());
                }
                if (matchedSite.getAccessibility() != null && !matchedSite.getAccessibility().isEmpty()) {
                    sb.append("\n🚗 **Accessibility:** ").append(matchedSite.getAccessibility());
                }
                return sb.toString();
            }

            // Why famous / significance query
            if (qLower.contains("why") || qLower.contains("famous") || qLower.contains("special") || qLower.contains("significan") || qLower.contains("importance")) {
                StringBuilder sb = new StringBuilder();
                sb.append("**").append(matchedSite.getSiteName()).append(" (").append(matchedSite.getState()).append(")**\n\n");
                sb.append(matchedSite.getSiteName()).append(" is renowned as a famous ").append(matchedSite.getCategory().toLowerCase()).append(" in ").append(matchedSite.getState()).append(".");
                if (matchedSite.isUnesco()) {
                    sb.append(" It holds **UNESCO World Heritage Site** status.");
                }
                sb.append("\n\n**Key Highlights:**\n");
                sb.append("• **Category:** ").append(matchedSite.getCategory()).append("\n");
                sb.append("• **Rating:** ⭐ ").append(matchedSite.getRating()).append("/5.0\n");
                if (matchedSite.getHistory() != null) {
                    sb.append("• **Heritage Feature:** ").append(matchedSite.getHistory()).append("\n");
                }
                return sb.toString();
            }

            // Who built / History query
            if (qLower.contains("who built") || qLower.contains("builder") || qLower.contains("king") || qLower.contains("dynasty") || qLower.contains("history") || qLower.contains("origin") || qLower.contains("created") || qLower.contains("founded")) {
                StringBuilder sb = new StringBuilder();
                sb.append("**History of ").append(matchedSite.getSiteName()).append("**\n\n");
                sb.append(matchedSite.getHistory() != null ? matchedSite.getHistory() : matchedSite.getSiteName() + " is an ancient monument representing India's rich architectural legacy.");
                if (matchedSite.getSiteName().equalsIgnoreCase("Brihadeeswarar Temple")) {
                    sb.append("\n\nIt was commissioned by Great Chola Emperor Rajaraja I and completed around 1010 CE.");
                } else if (matchedSite.getSiteName().equalsIgnoreCase("Hampi")) {
                    sb.append("\n\nHampi was the capital of the Vijayanagara Empire during the 14th to 16th centuries.");
                } else if (matchedSite.getSiteName().toLowerCase().contains("mahabalipuram")) {
                    sb.append("\n\nIt was established as a port city by the Pallava Dynasty during the 7th and 8th centuries.");
                }
                return sb.toString();
            }

            // What to see / attractions query
            if (qLower.contains("see") || qLower.contains("attraction") || qLower.contains("visit") || qLower.contains("things to do") || qLower.contains("what can i")) {
                StringBuilder sb = new StringBuilder();
                sb.append("**Attractions at ").append(matchedSite.getSiteName()).append("**\n\n");
                sb.append("When visiting ").append(matchedSite.getSiteName()).append(" in ").append(matchedSite.getState()).append(", you can explore:\n\n");
                sb.append("• **Main Heritage Site:** ").append(matchedSite.getSiteName()).append(" (").append(matchedSite.getCategory()).append(")\n");
                if (matchedSite.getNearbyAttraction() != null && !matchedSite.getNearbyAttraction().isEmpty()) {
                    sb.append("• **Nearby Attraction:** ").append(matchedSite.getNearbyAttraction()).append("\n");
                }
                if (matchedSite.getFestival() != null && !matchedSite.getFestival().isEmpty() && !matchedSite.getFestival().equalsIgnoreCase("none")) {
                    sb.append("• **Cultural Festival:** ").append(matchedSite.getFestival()).append("\n");
                }
                sb.append("• **Best Visit Season:** ").append(matchedSite.getBestTravelTime() != null ? matchedSite.getBestTravelTime() : "October to March").append("\n");
                return sb.toString();
            }

            // Budget / Cost query
            if (qLower.contains("budget") || qLower.contains("cost") || qLower.contains("price") || qLower.contains("expense") || qLower.contains("ticket") || qLower.contains("how much")) {
                return "**Estimated Budget for " + matchedSite.getSiteName() + "**\n\n" +
                       "• **Estimated Trip Cost:** Approx. ₹" + String.format("%.0f", matchedSite.getEstimatedBudget()) + " per person.\n" +
                       "• **Category:** " + matchedSite.getCategory() + "\n" +
                       "• **State:** " + matchedSite.getState() + "\n" +
                       "• **Best Time:** " + (matchedSite.getBestTravelTime() != null ? matchedSite.getBestTravelTime() : "October to March") + "\n\n" +
                       "*(Includes local travel, entry tickets, and average sightseeing expenses.)*";
            }

            // UNESCO query
            if (qLower.contains("unesco")) {
                if (matchedSite.isUnesco()) {
                    return "Yes! **" + matchedSite.getSiteName() + "** is an official **UNESCO World Heritage Site** recognized for its outstanding universal cultural value.";
                } else {
                    return "**" + matchedSite.getSiteName() + "** is a prominent protected national heritage site in " + matchedSite.getState() + ". While not currently listed as a UNESCO World Heritage site, it holds deep historical and cultural value.";
                }
            }

            // General inquiry about matched site
            StringBuilder sb = new StringBuilder();
            sb.append("**").append(matchedSite.getSiteName()).append(", ").append(matchedSite.getState()).append("**\n\n");
            sb.append(matchedSite.getHistory() != null ? matchedSite.getHistory() : matchedSite.getSiteName() + " is a premier cultural destination in India.").append("\n\n");
            sb.append("**Quick Facts:**\n");
            sb.append("• **Category:** ").append(matchedSite.getCategory()).append("\n");
            sb.append("• **Best Time to Visit:** ").append(matchedSite.getBestTravelTime()).append("\n");
            sb.append("• **Estimated Budget:** ₹").append(String.format("%.0f", matchedSite.getEstimatedBudget())).append("\n");
            if (matchedSite.isUnesco()) {
                sb.append("• **UNESCO Status:** World Heritage Site\n");
            }
            return sb.toString();
        }

        // 2. Recommendation queries
        if (qLower.contains("suggest") || qLower.contains("recommend") || qLower.contains("places to visit") || qLower.contains("trip") || qLower.contains("list")) {
            // Check for state matching
            String targetState = null;
            for (HeritageSite s : sites) {
                if (qLower.contains(s.getState().toLowerCase())) {
                    targetState = s.getState();
                    break;
                }
            }

            if (targetState != null) {
                ArrayList<HeritageSite> stateSites = filterByState(targetState);
                StringBuilder sb = new StringBuilder();
                sb.append("**Top Heritage Destinations in ").append(targetState).append(":**\n\n");
                int limit = Math.min(5, stateSites.size());
                for (int i = 0; i < limit; i++) {
                    HeritageSite s = stateSites.get(i);
                    sb.append(i + 1).append(". **").append(s.getSiteName()).append("** (").append(s.getCategory()).append(")");
                    if (s.isUnesco()) sb.append(" — *UNESCO World Heritage*");
                    sb.append("\n   📍 ").append(s.getState()).append(" • ⭐ ").append(s.getRating()).append(" • Approx. ₹").append(String.format("%.0f", s.getEstimatedBudget())).append("\n\n");
                }
                return sb.toString();
            }

            // Check for UNESCO recommendation
            if (qLower.contains("unesco")) {
                ArrayList<HeritageSite> unescoList = getUnescoSites();
                StringBuilder sb = new StringBuilder();
                sb.append("**Featured UNESCO World Heritage Sites in India:**\n\n");
                int limit = Math.min(5, unescoList.size());
                for (int i = 0; i < limit; i++) {
                    HeritageSite s = unescoList.get(i);
                    sb.append("• **").append(s.getSiteName()).append("** (").append(s.getState()).append(") — ").append(s.getCategory()).append("\n");
                }
                return sb.toString();
            }
        }

        // 3. Fallback General Knowledge Engine for broader heritage questions
        if (qLower.contains("mahabalipuram")) {
            return "**Mahabalipuram (Mamallapuram), Tamil Nadu**\n\n" +
                   "Mahabalipuram is a 7th-century coastal town renowned for its rock-cut temples and monuments carved by the Pallava Dynasty.\n\n" +
                   "**Famous Monuments:**\n" +
                   "• **Shore Temple:** Built by King Narasimhavarman II overlooking the Bay of Bengal.\n" +
                   "• **Pancha Rathas:** Monolithic rock-cut chariots named after the Pandavas.\n" +
                   "• **Arjuna's Penance:** A massive open-air relief depicting mythic scenes.\n\n" +
                   "It is a UNESCO World Heritage Site and a major cultural destination near Chennai.";
        }

        if (qLower.contains("hampi")) {
            return "**Hampi, Karnataka**\n\n" +
                   "Hampi was the majestic capital of the 14th-century Vijayanagara Empire, located along the Tungabhadra River.\n\n" +
                   "**Famous for:**\n" +
                   "• **Virupaksha Temple:** Active ancient temple dedicated to Lord Shiva.\n" +
                   "• **Vittala Temple & Stone Chariot:** Iconic Dravidian architecture.\n" +
                   "• **Royal Enclosure & Lotus Mahal:** Royal structures showcasing fusion design.\n\n" +
                   "Hampi is a UNESCO World Heritage Site famous worldwide for its rocky landscape and ancient ruins.";
        }

        if (qLower.contains("oldest temple") || qLower.contains("ancient temple")) {
            return "**Ancient Temples of India**\n\n" +
                   "India is home to some of the oldest active structural temples in the world:\n\n" +
                   "1. **Mundeshwari Temple (Bihar):** Dates back to ~108 CE, considered one of the oldest functional Hindu temples.\n" +
                   "2. **Shore Temple & Kailasanathar Temple (Tamil Nadu):** Built in the 7th-8th century by the Pallavas.\n" +
                   "3. **Brihadeeswarar Temple (Tamil Nadu):** Completed in 1010 CE by Emperor Rajaraja Chola I.\n" +
                   "4. **Badami Cave Temples (Karnataka):** Carved in the 6th century by the Chalukyas.";
        }

        // Default intelligent response
        return "I am **SPANVIA AI**, your dedicated heritage & tourism guide. You can ask me about:\n\n" +
               "• Specific heritage sites (e.g. *\"Where is Hampi?\"*, *\"Why is Mahabalipuram famous?\"*)\n" +
               "• History & Rulers (e.g. *\"Who built Brihadeeswarar Temple?\"*)\n" +
               "• State recommendations (e.g. *\"Suggest heritage places in Tamil Nadu\"*)\n" +
               "• Travel planning, best seasons, UNESCO sites & budget tips!";
    }
}