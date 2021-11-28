package ru.vlsv.server;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * NotificationService.
 *
 * @author Anatoly Lebedev
 * @version 1.0.0 27.11.2021
 * @link https://github.com/Centnerman
 */

/*
Host: smtp.gmail.com
Use Authentication: Yes
Port for TLS/STARTTLS: 587
Port for SSL: 465
 */

public class EmailSender {

    private final String username;
    private final String password;
    private final Properties props;

    public EmailSender(String username, String password) {
        this.username = username;
        this.password = password;

        props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", "587");
        props.put("mail.transport.protocol", "smtp");
//        props.put("mail.debug", "true");
    }

    public String send(String fromEmail, String toEmail, String subject, String text) {
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            //от кого
            message.setFrom(new InternetAddress(username));
            //кому
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            //Заголовок письма
            message.setSubject(subject);
            //Содержимое
            message.setText(text);

            //Отправляем сообщение
            Transport.send(message);
            return "Напоминание отправлено на адрес " + toEmail;

        } catch (MessagingException e) {
            e.printStackTrace();
            return "Ошибка отправки на адрес " + toEmail;
        }

    }
}
