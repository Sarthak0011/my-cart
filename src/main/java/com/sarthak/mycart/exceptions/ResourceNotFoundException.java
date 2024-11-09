package com.sarthak.mycart.exceptions;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String prefix){
        super(prefix + " not found!");
    }
}
