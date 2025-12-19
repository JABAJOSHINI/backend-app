package com.digital.digitalcommunicationservice.controller;

import com.digital.digitalcommunicationservice.abstraction.MailNotification;
import com.digital.digitalcommunicationservice.model.MessageRequest;
import com.digital.digitalcommunicationservice.service.MailNotificationService;
import com.example.appcommons.utility.Response;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.io.IOException;
/*
import javax.mail.MessagingException;*/

@RestController
public class DigitalController {

    @Autowired
    private MailNotificationService MailNotificationService;
    @PostMapping("/send-mail")
    public Response sendMail(@RequestBody MessageRequest request) throws MessagingException, TemplateException, IOException {
        return MailNotificationService.send(request);
    }
   /* @PostMapping("/sendMail")
    public String sendMail(@RequestBody MessageRequest request)
    {
        String status = MailNotificationService.send(request);
        return status;
    }*/
}
