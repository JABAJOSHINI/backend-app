package com.marine.productservice.exception;

public class ProductAlreadyExistsException extends APIException {
    public ProductAlreadyExistsException(String message) {
        super(message);
    }
}

