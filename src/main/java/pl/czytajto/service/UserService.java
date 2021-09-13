package pl.czytajto.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import pl.czytajto.registration.VerificationToken;
import pl.czytajto.registration.VerificationTokenRepository;
import pl.czytajto.user.model.User;
import pl.czytajto.user.model.UserRole;
import pl.czytajto.user.repository.UserRepository;
import pl.czytajto.user.repository.UserRoleRepository;

@Service
public class UserService {

	private static final String DEFAULT_ROLE = "ROLE_USER";
	private UserRepository userRepository;
	private UserRoleRepository roleRepository;
	private PasswordEncoder passwordEncoder;
	@Autowired
	private VerificationTokenRepository tokenRepository;
	
	@Autowired
	public UserService(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Autowired
	public void setRoleRepository(UserRoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}
		
	public void addWithDefaultRole(User user) {
				
		UserRole defauRole = roleRepository.findByRole(DEFAULT_ROLE);
		user.getRoles().add(defauRole);
		String passwordHash =  passwordEncoder.encode(user.getPassword());
		user.setPassword(passwordHash);
		userRepository.save(user);		
	}
	
	public User getUserByToken (String token) {
		
		VerificationToken token2 = tokenRepository.findByToken(token);
		User user = token2.getUser();

		return user;
	}
	
	public VerificationToken getVerificationToken (String token) {
		
		return tokenRepository.findByToken(token);
		
	}

	public void createVerificationToken(User user, String token) {
		
		VerificationToken myToken = new VerificationToken(token,user);
		tokenRepository.save(myToken);
	}
	
	public void saveRegistredUser (User user) {
		
		userRepository.save(user);
	}
		
	public boolean ifEmailExist (String email) {
		
		List<User> users = userRepository.findByEmail(email);
		
		return !users.isEmpty();
		
	}
	
	public boolean ifUsernameExist (String username) {
		
		return userRepository.findByUsername(username)!=null;
		
	}
}
