package com.uade.tpo.demo.controllers.domain;

import jakarta.mail.Multipart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmailFileDTO {

    private String[] toUser;

    private String subject;

    private String message;

    private Multipart file;
}
