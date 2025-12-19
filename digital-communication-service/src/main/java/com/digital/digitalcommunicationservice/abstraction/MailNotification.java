package com.digital.digitalcommunicationservice.abstraction;

import com.digital.digitalcommunicationservice.model.MessageRequest;
import com.example.appcommons.utility.Response;

public interface MailNotification {
    Response send(MessageRequest request);
}
