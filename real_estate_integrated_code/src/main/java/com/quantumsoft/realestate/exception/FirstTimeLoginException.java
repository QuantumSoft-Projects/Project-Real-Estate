package com.quantumsoft.realestate.exception;

public class FirstTimeLoginException extends RuntimeException {
    public FirstTimeLoginException(String message) {
        super(message);
    }
}
