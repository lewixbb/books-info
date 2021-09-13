package pl.czytajto.registration;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.czytajto.user.model.User;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
	
	VerificationToken findByToken(String token);
	VerificationToken findByUser(String user);
}
