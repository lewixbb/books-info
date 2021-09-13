package pl.czytajto.registration;

import java.util.UUID;

import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.stereotype.Component;

import pl.czytajto.service.UserService;
import pl.czytajto.user.model.User;

@Component
public class RegistrationListener implements ApplicationListener<RegistrationCompleteEvent>{

	@Autowired
	private UserService service;
	
	@Autowired
	private JavaMailSender mailSender;
		
	@Override
	public void onApplicationEvent (RegistrationCompleteEvent event) {
		this.confirmRegistration(event);
	}
	
		private void confirmRegistration(RegistrationCompleteEvent event) {
		User user = event.getUser();
		String token = UUID.randomUUID().toString();
		service.createVerificationToken(user, token);
		
		String recipientAdress = user.getEmail();
		String subject = "Registration Confirmation";
		String confirmationUrl = event.getAppUrl() + "/registrationConfirm?token=" + token;
				
		MimeMessage mess = this.mailSender.createMimeMessage();
		try {
			mess.addRecipients(Message.RecipientType.TO,recipientAdress);
			mess.setFrom("przemek.java.test@interia.pl");
			mess.setSubject(subject);
			mess.setText("<a href=" + "http://localhost:8080" + confirmationUrl + ">Aktywacja konta</a>","utf-8","html");
			mailSender.send(mess);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		
				
	}

}
