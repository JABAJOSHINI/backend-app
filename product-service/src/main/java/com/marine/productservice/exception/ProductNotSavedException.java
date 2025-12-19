package com.marine.productservice.exception;

public class ProductNotSavedException extends APIException {
    public ProductNotSavedException(String message) {
        super(message);
    }
}