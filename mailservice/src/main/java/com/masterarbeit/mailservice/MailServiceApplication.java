package com.masterarbeit.mailservice;

import com.masterarbeit.mailservice.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MailServiceApplication {
    @Autowired
    private EmailService emailService;
    public static void main(String[] args) {
        SpringApplication.run(MailServiceApplication.class, args);
    }

//    @EventListener(ApplicationReadyEvent.class)
//    public void sendMail() {
//        emailService.sendEmail("s36838@bht-berlin.de","This is Subject","This is body");
//    }
}
