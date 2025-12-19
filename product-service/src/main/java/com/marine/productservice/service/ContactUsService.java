package com.marine.productservice.service;

import com.marine.productservice.controller.ContactUsController;
import com.marine.productservice.controller.ProductController;
import com.marine.productservice.entity.ClientEntity;
import com.marine.productservice.model.ContactModel;
import com.marine.productservice.model.EnquiryModel;
import com.marine.productservice.repository.ClientRepository;
import com.marine.productservice.util.DtoToDao;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class ContactUsService {
    @Autowired
    private DtoToDao dtoToDao;
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;
    @Value("${to-email}")
    private String toEmail;
    @Autowired
    private ClientRepository clientRepository;

    Logger logger = LoggerFactory.getLogger(ContactUsController.class);

    public void createCustomerContact(ContactModel body) {
      // ClientEntity clientDetails = saveClientDetails(body);
        try {
            SendCustomerEnquiryEmail(toEmail,body);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        logger.debug("mail send successfully!!!");
    }
    private ClientEntity saveClientDetails(ContactModel body) {
        ClientEntity clientEntity = new ClientEntity();
        ClientEntity clientDetailsToSave = dtoToDao.saveCusomerContacts(clientEntity, body);
        clientRepository.save(clientDetailsToSave);
        return clientDetailsToSave;
    }
    public void SendCustomerEnquiryEmail(String to, ContactModel body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        String htmlContent = null;
        try {
            helper = new MimeMessageHelper(message, true);
            // Prepare template model
            Map<String, Object> templateModel = new HashMap<>();
            templateModel.put("name", body.getFullName());
            templateModel.put("email", body.getEmail());
            templateModel.put("message", body.getMessage());
            templateModel.put("phone", body.getPhone());

            // Prepare Thymeleaf template
            Context context = new Context();
            context.setVariables(templateModel);
            htmlContent = templateEngine.process("customer-enquiry-email", context);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        try {
            helper.setTo(to);

            helper.setSubject("Enquiry Email From Client");
            helper.setText(htmlContent, true);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        try {
            mailSender.send(message);
        } catch (MailAuthenticationException e) {
            e.printStackTrace();
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }
    }
}
