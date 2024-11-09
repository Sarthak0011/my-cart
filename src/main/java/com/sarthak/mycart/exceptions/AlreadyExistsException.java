package com.sarthak.mycart.exceptions;

public class AlreadyExistsException extends RuntimeException{
    public AlreadyExistsException(String prefix){
        super(prefix + " already exists!");
    }
}
