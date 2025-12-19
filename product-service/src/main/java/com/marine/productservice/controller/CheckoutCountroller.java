package com.marine.productservice.controller;

import com.example.appcommons.utility.Response;
import com.marine.productservice.api.CheckoutApi;
import com.marine.productservice.entity.Products;
import com.marine.productservice.model.checkoutModel;
import com.marine.productservice.service.CheckoutService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class CheckoutCountroller implements CheckoutApi {
    Logger logger = LoggerFactory.getLogger(CheckoutCountroller.class);
    @Autowired
    private CheckoutService checkoutService;
    @Override
    public String sendCheckoutEmail(checkoutModel checkoutItems) throws MessagingException, IOException {

        checkoutService.sendEnquiryEmailToAdmin(checkoutItems);
        return "Email sent successfully!";
    }
}
