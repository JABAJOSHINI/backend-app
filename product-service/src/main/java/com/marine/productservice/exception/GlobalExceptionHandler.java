package com.marine.productservice.exception;

import com.example.appcommons.utility.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ProductAlreadyExistsException.class, ProductNotFoundException.class})
    public ResponseEntity<Response> handleProductExists(APIException ex) {
        Response response = new Response();
        response.setMessage(ex.getMessage());
        response.setStatus(true);
        response.setData(null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}