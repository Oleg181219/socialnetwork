package JavaPRO.services;

import JavaPRO.api.request.MailRequest;
import JavaPRO.api.response.*;
import JavaPRO.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

@Service
public class EmailService {

    @Autowired
    JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String username;

    public void sendMail(String subject, String messageBody, String email){
        new Thread(() -> {
            MimeMessage message = javaMailSender.createMimeMessage();
            try {
                MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
                messageHelper.setFrom(username);
                messageHelper.setTo(email);
                messageHelper.setSubject(subject);
                message.setContent(messageBody, "text/html");
                javaMailSender.send(message);
            } catch (MessagingException e){
                e.printStackTrace();
            }
        }).start();
    }

    public ResponseEntity<Response> sendMailToSupport(MailRequest mailRequest){
        if (mailRequest.getEmail() == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new OkResponse("BAD REQUEST", new Date().getTime(), new ResponseData("NO EMAIL FOUND")));
        }
        if (mailRequest.getText() == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new OkResponse("BAD REQUEST", new Date().getTime(), new ResponseData("NO TEXT MESSAGE FOUND")));
        }
       sendMail((Config.STRING_MAIL_TO_SUPPORT_SUBJECT + mailRequest.getEmail()), mailRequest.getText(), mailRequest.getEmail());
        return ResponseEntity
                .ok(new OkResponse("SUCCESS", new Date().getTime(), new ResponseData(Config.STRING_MAIL_TO_SUPPORT_RESPONSE)));
    }
}
