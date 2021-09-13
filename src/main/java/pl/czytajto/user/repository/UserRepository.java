package pl.czytajto.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.czytajto.user.model.User;

public interface UserRepository extends JpaRepository<User, String> {

	User findByUsername(String username);
	List <User> findByEmail(String email);
	
}
