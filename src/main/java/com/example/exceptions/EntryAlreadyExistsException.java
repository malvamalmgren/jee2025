package com.example.exceptions;

public class EntryAlreadyExistsException extends RuntimeException {
    public EntryAlreadyExistsException() {
        super();
    }
    public EntryAlreadyExistsException(String message) {
        super(message);
    }
}
