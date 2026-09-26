package com.pooja.spanvia.exception;

/**
 * Custom exception thrown when a requested heritage site cannot be found in the dataset.
 */
public class SiteNotFoundException extends Exception {

    public SiteNotFoundException(String message) {
        super(message);
    }

    public SiteNotFoundException(int siteId) {
        super("Heritage site with ID " + siteId + " was not found in the dataset.");
    }
}
