package com.marine.productservice.api;

import com.marine.productservice.model.checkoutModel;
import jakarta.mail.MessagingException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@RequestMapping("/api/checkout")
public interface CheckoutApi {
    @PostMapping("/sendEmail")
    public String sendCheckoutEmail(@RequestBody checkoutModel checkoutItems) throws MessagingException, IOException;
}
