package com.scm.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.scm.services.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.scheduling.annotation.Async;

@Service
public class EmailServiceImpl implements EmailService {

    private Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired(required = false)
    private JavaMailSender eMailSender;

    @Value("${spring.mail.username}")
    private String domainName;

    @Override
    @Async
    public void sendEmail(String to, String subject, String body) {
        if (eMailSender == null) {
            logger.warn("Email service not configured. Skipping email.");
            return;
        }
        logger.info("Preparing to send plain text email to: {}", to);
        MimeMessage mimeMessage = eMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(domainName);
            helper.setText(body);
            eMailSender.send(mimeMessage);
            logger.info("Plain text email sent successfully to: {}", to);
        } catch (MessagingException e) {
            logger.error("Error while sending email to {}: {}", to, e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }

    @Override
    @Async
    public void sendEmailWithHtml(String to, String subject, String htmlContent) {
        logger.info("Preparing to send HTML email to: {}", to);
        MimeMessage mimeMessage = eMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(domainName);
            helper.setText(htmlContent, true);
            eMailSender.send(mimeMessage);
            logger.info("HTML email sent successfully to: {}", to);
        } catch (MessagingException e) {
            logger.error("Error while sending HTML email to {}: {}", to, e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }

    @Override
    public void sendEmailWithAttachment() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendEmailWithAttachment'");
    }

}
