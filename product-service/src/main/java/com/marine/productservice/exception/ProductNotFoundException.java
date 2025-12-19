package com.marine.productservice.exception;

public class ProductNotFoundException extends APIException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}