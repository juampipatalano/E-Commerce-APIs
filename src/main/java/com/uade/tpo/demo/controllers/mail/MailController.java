package com.uade.tpo.demo.controllers.mail;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.demo.controllers.domain.EmailDTO;
import com.uade.tpo.demo.service.EmailService;

import io.micrometer.core.ipc.http.HttpSender.Response;

@RestController
@RequestMapping("/api/mail")
public class MailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/sendMessage")
    public ResponseEntity<?> receiveRequestEmail(@RequestBody EmailDTO emailDTO) {

        System.out.println("Email received: " + emailDTO);

        emailService.sendEmail(emailDTO.getToUser(),
            emailDTO.getSubject(),
          emailDTO.getMessage());

        Map<String, String> response = new HashMap<>();

        response.put("enviado", "Enviado");

        return ResponseEntity.ok(response);
    }
    


}
