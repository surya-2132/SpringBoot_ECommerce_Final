package com.fullstackproject.ecommerce.Exception;

import lombok.Getter;

import java.io.Serial;

@Getter
public class CategoryException extends Exception{

    private final String exceptionName;
    @Serial
    private static final long serialVersionUID = 1L;

    public CategoryException(String exceptionName) {
        super(exceptionName);   //To get/print message from parent Exception class to derived class of custom exception
        this.exceptionName = exceptionName;
    }

}
