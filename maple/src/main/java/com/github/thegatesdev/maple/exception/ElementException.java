package com.github.thegatesdev.maple.exception;

public class ElementException extends RuntimeException {

    public ElementException(String msg) {
        super(msg);
    }

    public ElementException(String msg, Throwable cause){
        super(msg, cause);
    }

    public ElementException(Throwable cause){
        super(cause);
    }
}
