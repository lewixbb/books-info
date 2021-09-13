package pl.czytajto.security;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.czytajto.user.model.User;
import pl.czytajto.user.repository.UserRepository;

@Controller
public class AuthenticationController {

	@Autowired
	private UserRepository userRepository;
	
	@PostMapping("/login")
	@ResponseBody
	public ResponseEntity<?> login (Principal user) {
		
		User isActive = userRepository.findByUsername(user.getName());
		if(isActive.isEnabled()) {
		
				System.out.println("nieaktywny");
				return (ResponseEntity<?>) ResponseEntity.status(HttpStatus.SEE_OTHER);
			
		} else {

				return ResponseEntity.ok(user);
				
				}
	}
	
	
}


