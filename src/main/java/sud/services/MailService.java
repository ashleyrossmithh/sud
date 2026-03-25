package sud.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    public JavaMailSender emailSender;

    public void sendSimpleEmail(String toAddress, String newStatus) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(toAddress);
        simpleMailMessage.setSubject("Смена статуса судебного дела");
        simpleMailMessage.setText("Статус судебного дела изменился на: " + newStatus);
        emailSender.send(simpleMailMessage);
    }

}
