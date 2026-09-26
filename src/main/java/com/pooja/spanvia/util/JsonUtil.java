package com.pooja.spanvia.util;

import com.pooja.spanvia.model.HeritageSite;
import java.util.List;

public class JsonUtil {

    public static String escapeJson(String input) {
        if (input == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            switch (c) {
                case '"': builder.append("\\\""); break;
                case '\\': builder.append("\\\\"); break;
                case '\b': builder.append("\\b"); break;
                case '\f': builder.append("\\f"); break;
                case '\n': builder.append("\\n"); break;
                case '\r': builder.append("\\r"); break;
                case '\t': builder.append("\\t"); break;
                default:
                    if (c < ' ') {
                        builder.append(String.format("\\u%04x", (int) c));
                    } else {
                        builder.append(c);
                    }
            }
        }
        return builder.toString();
    }

    public static String toJson(HeritageSite site) {
        if (site == null) {
            return "null";
        }
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        sb.append("\"id\":").append(site.getSiteId()).append(",");
        sb.append("\"name\":\"").append(escapeJson(site.getSiteName())).append("\",");
        sb.append("\"state\":\"").append(escapeJson(site.getState())).append("\",");
        sb.append("\"category\":\"").append(escapeJson(site.getCategory())).append("\",");
        sb.append("\"history\":\"").append(escapeJson(site.getHistory())).append("\",");
        sb.append("\"bestTime\":\"").append(escapeJson(site.getBestTravelTime())).append("\",");
        sb.append("\"budget\":").append(site.getEstimatedBudget()).append(",");
        sb.append("\"unesco\":").append(site.isUnesco()).append(",");
        sb.append("\"festival\":\"").append(escapeJson(site.getFestival())).append("\",");
        sb.append("\"nearbyAttraction\":\"").append(escapeJson(site.getNearbyAttraction())).append("\",");
        sb.append("\"accessibility\":\"").append(escapeJson(site.getAccessibility())).append("\",");
        sb.append("\"rating\":").append(site.getRating());
        sb.append("}");
        return sb.toString();
    }

    public static String toJsonList(List<HeritageSite> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                sb.append(toJson(list.get(i)));
                if (i < list.size() - 1) {
                    sb.append(",");
                }
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public static String toStatsJson(int totalSites, int unescoCount, int statesCount) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"totalSites\":").append(totalSites).append(",");
        sb.append("\"unescoSites\":").append(unescoCount).append(",");
        sb.append("\"statesCovered\":").append(statesCount);
        sb.append("}");
        return sb.toString();
    }

    public static String toFestivalsJson(String[] festivals, List<HeritageSite> sitesWithFestivals) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"featuredFestivals\":[");
        if (festivals != null) {
            for (int i = 0; i < festivals.length; i++) {
                sb.append("\"").append(escapeJson(festivals[i])).append("\"");
                if (i < festivals.length - 1) sb.append(",");
            }
        }
        sb.append("],");
        sb.append("\"sites\":").append(toJsonList(sitesWithFestivals));
        sb.append("}");
        return sb.toString();
    }
}
