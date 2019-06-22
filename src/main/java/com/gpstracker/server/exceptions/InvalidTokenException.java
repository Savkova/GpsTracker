package com.gpstracker.server.exceptions;

public class InvalidTokenException extends Exception {

    public InvalidTokenException() {
        super("Invalid token");
    }

}
