package com.application.exception;

public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String key, String locale) {
        super("Translation already exists with key: " + key + " and locale: " + locale);
    }

    public static DuplicateResourceException forTag(String name) {
        return new DuplicateResourceException("Tag already exists with name: " + name);
    }
}