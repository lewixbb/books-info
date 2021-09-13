package pl.czytajto.controller;

import java.lang.ProcessBuilder.Redirect;
import java.util.Calendar;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import pl.czytajto.registration.RegistrationCompleteEvent;
import pl.czytajto.registration.VerificationToken;
import pl.czytajto.registration.VerificationTokenRepository;
import pl.czytajto.service.UserService;
import pl.czytajto.user.model.User;
import pl.czytajto.user.repository.UserRepository;

@RestController
public class RegistrationController {

	
	private UserService userService;

//	private VerificationTokenRepository findToken;

	@Autowired
	private ApplicationEventPublisher eventPublisher;
		
	@Autowired
	public RegistrationController(UserService userService) {
		super();
		this.userService = userService;
	}
	
//	@GetMapping("/registration")
//	public String register(Model model) {
//		System.out.println("get");
//		model.addAttribute("user",new User());
//		return "registration";
//	}
	
	@PostMapping("/registration")
	public ResponseEntity<?> addUser(@ModelAttribute ("User") @Valid User user, BindingResult bindResult, HttpServletRequest request) {
		
		System.out.println("poczatek rejestracji");
		
		if(bindResult.hasErrors()) {
									
			Object[] errors = bindResult.getFieldErrors().toArray();	
		
			return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);	
			
		} else {
				
			userService.addWithDefaultRole(user);
			RegistrationCompleteEvent event = new RegistrationCompleteEvent(user,request.getLocale(),request.getContextPath());
			eventPublisher.publishEvent(event);
			
			System.out.println("dodajemy uzytkownika");
		
			return new ResponseEntity<>(HttpStatus.CREATED);
				}
	}
	
	@GetMapping("/registrationConfirm")
	public RedirectView confrimRegistration(@RequestParam("token") String token) {
			
			VerificationToken verificationToken = userService.getVerificationToken(token);
			User user = userService.getUserByToken(token);
			Calendar cal = Calendar.getInstance();
			if((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <=0) {
				
				return new RedirectView("/#!/registrationExpired");
				
			} else {
			
			user.setEnabled(true);		
			userService.saveRegistredUser(user);
			
				return new RedirectView("/#!/registrationConfirmed");
			}
	}
	
	@GetMapping("/dupa")
	public RedirectView dupa () {
		System.out.println("dupa");
		return new RedirectView("/#!/registrationConfirmed");
	}
	
}
