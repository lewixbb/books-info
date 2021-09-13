package pl.czytajto.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class MailCfg {
	
	
	@Bean
	public JavaMailSender cfgJavaMailSender() {
		
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		
		mailSender.setHost("poczta.interia.pl");
		mailSender.setPort(587);
		
		String username = "przemek.java.test@interia.pl";
		String password = "MXabJudn2PEcDPh";
		
		mailSender.setUsername(username);
		mailSender.setPassword(password);
				
		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");

		props.put("SSLOnConnect","true");
		props.put("mail.smtop.host","poczta.interia.pl");
		props.put("mail.smtp.port", "587");
		
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", "true");
						
		return mailSender;
		
	}

}
