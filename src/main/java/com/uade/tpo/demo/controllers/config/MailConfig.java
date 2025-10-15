package com.uade.tpo.demo.controllers.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import com.fasterxml.jackson.databind.annotation.JsonAppend.Prop;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class MailConfig {

    @Value("${email.sender}")
    private String emailUser;

    @Value("${email.password}")
    private String emailPassword;

        @Bean
        public JavaMailSender getJavaMailSender() {
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost("smtp.gmail.com"); 
            mailSender.setPort(587);
            //Mail que vamos a usar para enviar los mails
            mailSender.setUsername(emailUser);
            //Contraseña de la app generada en google
            mailSender.setPassword(emailPassword);
            //Obtenemos las propiedades y seteamos las que necesitamos
            Properties props = mailSender.getJavaMailProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.debug", "true");
            return mailSender;
        }
}
