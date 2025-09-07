package com.application.exception;

public class TranslationNotFoundException extends RuntimeException {
    public TranslationNotFoundException(String message) {
        super(message);
    }

    public TranslationNotFoundException(Long id) {
        super("Translation not found with id: " + id);
    }

    public TranslationNotFoundException(String key, String locale) {
        super("Translation not found with key: " + key + " and locale: " + locale);
    }
}