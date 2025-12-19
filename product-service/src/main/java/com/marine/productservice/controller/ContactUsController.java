package com.marine.productservice.controller;

import com.example.appcommons.utility.Response;
import com.marine.productservice.api.ContactUsApi;
import com.marine.productservice.exception.APIException;
import com.marine.productservice.model.ContactModel;
import com.marine.productservice.service.ContactUsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContactUsController implements ContactUsApi {
    @Autowired
    private ContactUsService contactUsService;
    Logger logger = LoggerFactory.getLogger(ContactUsController.class);

    @Override
    public Response createCustomerContact(ContactModel body) throws Exception {
        logger.debug("Contact Us api command received!!!");
        Response response = new Response();
        try{
         if(body != null){
            contactUsService.createCustomerContact(body);
            logger.debug("Send Enquiry done!!!");
            response.setMessage("SUCCESS");
            response.setStatus(Boolean.FALSE);
        }}catch (APIException e) {
         logger.error("Contact Us api error >>>>>>>>> "+e.getMessage());
          throw e;
        }
         return response;
    }
}
