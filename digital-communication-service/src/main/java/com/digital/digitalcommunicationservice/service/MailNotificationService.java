package com.digital.digitalcommunicationservice.service;


import com.digital.digitalcommunicationservice.model.MessageRequest;
import com.digital.digitalcommunicationservice.utils.Utils;
import com.example.appcommons.utility.Response;


import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
/*import javax.mail.MessagingException;*/

@Service
public class MailNotificationService  {
    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String userName;

    @Autowired
    private Configuration freeMarkerConfiguration;
    public void sendEmail(String to, String subject, Map<String, Object> model) throws MessagingException, IOException, TemplateException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);

        String content = generateEmailContent("email-template.ftl", model);
        helper.setText(content, true);

        mailSender.send(message);
    }

    private String generateEmailContent(String templateName, Map<String, Object> model) throws IOException, TemplateException {
        Template template = freeMarkerConfiguration.getTemplate(templateName);
        StringWriter writer = new StringWriter();
        template.process(model, writer);
        return writer.toString();
    }
    public Response send(MessageRequest request) throws MessagingException, TemplateException, IOException {


        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject("RFQ email");
        helper.setFrom(request.getFromEmail());
        helper.setTo(userName);

        String emailContent = getEmailContent(request);
        helper.setText(emailContent, true);
        mailSender.send(mimeMessage);

        /*   SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(request.getFromEmail());
        message.setTo(userName);
        message.setText("");
        message.setSubject(request.getSubject());*/
       /* Map<String, Object> model = new HashMap<>();
        model.put("companyName",request.getCompanyName());
        model.put("description",request.getDescription());
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setSubject("RFQ email");
        helper.setFrom(request.getFromEmail());
        helper.setTo(userName);
        String content = generateEmailContent("email.ftlh", model);
        helper.setText(content, true);*/
      /*  boolean html = true;
        helper.setText("<b>Hey guys</b>,<br><i>Welcome to my new home</i>", html);
        String description = request.getDescription();

        helper.setText("<table>\n" +
                "  <tr>\n" +
                "    <th>Company</th>\n" +
                "    <th>Contact</th>\n" +
                "    <th>Country</th>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td>"+ description +"</td>\n" +
                "    <td>"+ description +"</td>\n" +
                "    <td>+ description +</td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td>Centro comercial Moctezuma</td>\n" +
                "    <td>Francisco Chang</td>\n" +
                "    <td>Mexico</td>\n" +
                "  </tr>\n" +
                "</table>", html);*/
     //   mailSender.send(message);

        System.out.println("Mail Sent Successfully....");
        Response response =Response.builder()
                .message("Message Sent")
                .sendDateTime(Utils.getCurrentISTTime())
                .data("")
                .build();
        return response;
    }
    String getEmailContent(MessageRequest request) throws IOException, TemplateException {
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("model", request);
        freeMarkerConfiguration.getTemplate("email.ftlh").process(model, stringWriter);
        return stringWriter.getBuffer().toString();
    }
   /* public Response send(MessageRequest request) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
try {
    helper = new MimeMessageHelper(message, true);
    helper.setFrom(request.getFromEmail());
    helper.setTo(userName);
    helper.setSubject(request.getSubject());
    //  helper.setText(text, true);
    mailSender.send(message);
}catch (Exception e){
    e.printStackTrace();
}

        Response response =Response.builder()
                .message("Message Sent")
                .sendDateTime(Utils.getCurrentISTTime())
                .data(message)
                .build();
        return response;
    }
*/
}
