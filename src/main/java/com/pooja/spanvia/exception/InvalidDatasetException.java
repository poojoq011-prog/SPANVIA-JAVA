package com.pooja.spanvia.exception;

/**
 * Custom exception thrown when there is an issue reading, parsing, or loading the heritage dataset.
 */
public class InvalidDatasetException extends Exception {

    public InvalidDatasetException(String message) {
        super(message);
    }

    public InvalidDatasetException(String message, Throwable cause) {
        super(message, cause);
    }
}
