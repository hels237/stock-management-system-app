package com.k48.stock_management_system.notificationConfig;


import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendConfirmationEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }

    public void sendReminderEmail(String to, String subject, String content) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(buildHtmlReminder(content), true);
        };
        mailSender.send(messagePreparator);
    }

    private String buildHtmlReminder(String content) {
        return "<html><body>" +
                "<h2>Notification</h2>" +
                "<p>" + content + "</p>" +
                "</body></html>";
    }
    
    public void sendSupplierOrderEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }
    
    public void sendClientConfirmationEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }

    public void sendCancellationEmail(String email, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }
}
