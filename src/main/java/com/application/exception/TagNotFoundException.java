package com.application.exception;

public class TagNotFoundException extends RuntimeException {
    public TagNotFoundException(String message) {
        super(message);
    }

    public TagNotFoundException(Long id) {
        super("Tag not found with id: " + id);
    }

    public static TagNotFoundException byName(String name) {
        return new TagNotFoundException("Tag not found with name: " + name);
    }
}