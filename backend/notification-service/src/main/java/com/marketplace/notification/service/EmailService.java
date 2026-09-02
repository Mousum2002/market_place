package com.marketplace.notification.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

  private final JavaMailSender mailSender;

  @Value("${app.mail.from}")
  private String fromEmail;

  @Value("${app.mail.subject-prefix:}")
  private String subjectPrefix;

  public void send(String to, String subject, String body) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(fromEmail);
    message.setTo(to);
    message.setSubject(buildSubject(subject));
    message.setText(body);
    mailSender.send(message);
  }

  private String buildSubject(String subject) {
    if (subjectPrefix == null || subjectPrefix.isBlank()) {
      return subject;
    }
    return subjectPrefix.trim() + " " + subject;
  }
}
