package org.example.mealwise.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService {

    private static void createEmailSession(String email,String message) throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        properties.put("mail.transport.protocol", "smtp");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(System.getenv("MEALWISE_EMAIL_USER"), System.getenv("MEALWISE_EMAIL_PASS"));
            }
        });

        Message msg = new MimeMessage(session);
        msg.setSubject("Mealwise: Your OTP for Password Reset");
        String htmlContent = "<html>" +
                "<body style='font-family: Arial, sans-serif; background-color: #f4f4f9; padding: 20px;'>" +
                "<div style='max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);'>" +
                "<h2 style='text-align: center; color: #333;'>Your OTP Code for Mealwise</h2>" +
                "<p style='font-size: 16px; color: #555;'>Hi,</p>" +
                "<p style='font-size: 16px; color: #555;'>You have requested a One-Time Password (OTP) for your Mealwise account. Please use the following OTP:</p>" +
                "<h3 style='text-align: center; font-size: 30px; color: #007BFF; font-weight: bold;'>" + message + "</h3>" +
                "<p style='font-size: 16px; color: #555;'>This OTP is valid for the next 10 minutes. If you did not request this, please ignore this message.</p>" +
                "<p style='font-size: 16px; color: #555;'>Thank you for using <strong>Mealwise</strong>!</p>" +
                "<p style='font-size: 14px; color: #aaa; text-align: center;'>This is an automated message. Please do not reply.</p>" +
                "</div>" +
                "</body>" +
                "</html>";
        msg.setContent(htmlContent, "text/html");

        Address recipient = new InternetAddress(email);
        msg.setRecipient(Message.RecipientType.TO, recipient);
        Transport.send(msg);
    }

    public static void sendPasswordResetEmail(String email, String code) {
        try {
            createEmailSession(email, code);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}