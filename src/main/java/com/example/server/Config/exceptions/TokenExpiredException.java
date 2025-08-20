package com.example.server.Config.exceptions;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException(String message) { super(message); }
}
