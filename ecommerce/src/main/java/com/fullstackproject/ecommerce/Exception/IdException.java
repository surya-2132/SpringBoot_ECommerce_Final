package com.fullstackproject.ecommerce.Exception;

import lombok.Getter;

@Getter
public class IdException extends Exception{

    private final String exceptionName;
    public IdException(String exceptionName) {
        super(exceptionName);    //To get/print message from parent Exception class to derived class of custom exception
        this.exceptionName = exceptionName;
    }
}
