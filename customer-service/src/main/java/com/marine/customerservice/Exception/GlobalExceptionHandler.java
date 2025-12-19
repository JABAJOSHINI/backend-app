package com.marine.customerservice.Exception;

import com.example.appcommons.utility.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 🔹 Common Method to Handle All Exceptions with Dynamic Messages
    private ResponseEntity<Object> buildResponseEntity(Exception ex, HttpStatus status) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("error", ex.getClass().getSimpleName()); // Exception class name
        body.put("message", ex.getMessage()); // Dynamic error message

        return new ResponseEntity<>(body, status);
    }

    // 🔹 Handle Generic Exception (Covers All Uncaught Exceptions)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> HandleGlobalException(Exception ex, WebRequest request) {
        return buildResponseEntity(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 🔹 Handle Spring Security's BadCredentialsException
//    @ExceptionHandler(BadCredentialsException.class)
//    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex) {
//        return buildResponseEntity(ex, HttpStatus.UNAUTHORIZED);
//    }

    // 🔹 Handle User Not Found Exception
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return buildResponseEntity(ex, HttpStatus.NOT_FOUND);
    }

    // 🔹 Handle Illegal Arguments (e.g., invalid parameters)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        return buildResponseEntity(ex, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler({ UserNotFoundException.class})
    @ResponseBody
    public ResponseEntity<Response> notFoundException(APIException ex) {
        Response response = new Response();
        response.setMessage(ex.getMessage());
        response.setStatus(false);
        response.setData(null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON) // FORCE JSON
                .body(response);
    }
    @ExceptionHandler({UserAlreadyExistException.class, PasswordNotMatchedException.class, BadCredentialsException.class})
    @ResponseBody
    public ResponseEntity<Response> userAlreadyExists(APIException ex) {
        Response response = new Response();
        response.setMessage(ex.getMessage());
        response.setStatus(true);
        response.setData(null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON) // FORCE JSON
                .body(response);
    }
}
