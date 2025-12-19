package com.digital.digitalcommunicationservice.components;

import com.digital.digitalcommunicationservice.abstraction.MailNotification;
import com.digital.digitalcommunicationservice.model.MessageRequest;
import com.digital.digitalcommunicationservice.utils.Utils;
import com.example.appcommons.utility.Response;
import com.google.common.collect.ImmutableList;
/*import it.ozimov.springboot.mail.configuration.EnableEmailTools;
import it.ozimov.springboot.mail.model.Email;
import it.ozimov.springboot.mail.model.defaultimpl.DefaultEmail;
import it.ozimov.springboot.mail.service.EmailService;*/
/*import jakarta.mail.internet.AddressException;*/
/*import jakarta.mail.internet.InternetAddress;*/
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
/*import javax.mail.internet.InternetAddress;*/


@Log4j2
@Component
/*@EnableEmailTools*/
public class MailNotificationComponent  {
    @Value("${spring.mail.username}")
    private String userName;
   /* @Autowired
    private EmailService emailService;*/


    public Response send(MessageRequest request) {
        log.info("Sending Mail notification : " + request.getFromEmail());
        String fromEmail = request.getFromEmail();
        String html = request.getDescription();
        try{
            if (request.getFromEmail().isEmpty()){
                Response response = Response.builder().message(null)
                        .sendDateTime(Utils.getCurrentISTTime())
                        .status(true)
                        .build();
                return response;
            }
/*
            final Email email = DefaultEmail.builder()
                    .from(new InternetAddress(request.getFromEmail()))
                    .to(ImmutableList.of(new InternetAddress(userName)))
                    .subject(request.getSubject())
                    .body(request.getMessage())
                    .encoding("UTF-8")
                    .build();

            log.info("Email Send Body: " + email);
            emailService.send(email);

            Response response =Response.builder()
                    .message("Message Sent")
                    .sendDateTime(Utils.getCurrentISTTime())
                    .build();*/

        }catch (Exception e) {
            e.printStackTrace();
            Response response = Response.builder().message(null)
                    .sendDateTime(Utils.getCurrentISTTime())
                    .status(true)
                    .build();
            return response;
        }
        return null;
    }

}
