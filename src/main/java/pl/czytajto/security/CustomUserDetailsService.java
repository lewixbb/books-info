package pl.czytajto.security;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import pl.czytajto.user.model.User;
import pl.czytajto.user.model.UserRole;
import pl.czytajto.user.repository.UserRepository;

@Component
public class CustomUserDetailsService implements UserDetailsService {

	private UserRepository userRepository;
	
	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		if(user == null)
			throw new UsernameNotFoundException("User not found");
		org.springframework.security.core.userdetails.User userDetails = 
				new org.springframework.security.core.userdetails.User(
						user.getUsername(),
						user.getPassword(),
						user.isEnabled(),
						true, true, true, converAuthorites(user.getRoles()));
		return userDetails;
	}

	private Set<GrantedAuthority> converAuthorites(Set<UserRole> userRoles) {
		Set<GrantedAuthority> authorites = new HashSet<GrantedAuthority>();
		for(UserRole ur:userRoles) {
			authorites.add(new SimpleGrantedAuthority(ur.getRole()));
		}
		return authorites;
	}

}
