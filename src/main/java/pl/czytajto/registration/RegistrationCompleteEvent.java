package pl.czytajto.registration;

import java.util.Locale;

import org.springframework.context.ApplicationEvent;

import lombok.Data;
import pl.czytajto.user.model.User;

public @Data class RegistrationCompleteEvent extends ApplicationEvent{

	private String appUrl;
	private Locale locale;
	private User user;
	
	public RegistrationCompleteEvent(User user, Locale locale, String appUrl) {
		super(user);
		this.appUrl = appUrl;
		this.locale = locale;
		this.user = user;
	}

	
	
	
	
}
