package com.pooja.spanvia.model;

/**
 * TempleSite is a subclass of HeritageSite.
 */
public class TempleSite extends HeritageSite {

    private String deity;

    public TempleSite(int siteId, String siteName,
                      String state, String history,
                      double estimatedBudget,
                      boolean unesco,
                      String deity) {

        super(siteId, siteName, state, history,
              estimatedBudget, unesco, "Temple");

        this.deity = deity;
    }

    @Override
    public void showCategory() {
        System.out.println(
            "Temple Heritage Site - Deity: " + deity);
    }
}