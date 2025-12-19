package com.marine.productservice.api;

import com.example.appcommons.utility.Response;
import com.marine.productservice.model.ContactModel;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@CrossOrigin(origins = "*", allowedHeaders = "*")

@RequestMapping("/api/contactus")
public interface ContactUsApi{
    @PostMapping
    public Response createCustomerContact(@Valid @RequestBody ContactModel body) throws Exception;
}