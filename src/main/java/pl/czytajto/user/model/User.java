package pl.czytajto.user.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import pl.czytajto.constraint.Matching;
import pl.czytajto.constraint.UniqueMail;
import pl.czytajto.constraint.UniqueUsername;

@Entity
@Matching(first = "password", second = "matchingPassword", message = "blabalgva")

public @Data class User implements Serializable {
		
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull
	@NotEmpty
	@UniqueUsername
	private String username;
	@NotNull
	@NotEmpty
	@Email
	@UniqueMail
	private String email;
	@NotNull
	@NotEmpty
	@Size(min=6, max=16)
	private String password;
	@Transient
	private String matchingPassword;
	@Column(name= "enabled")
	private boolean enabled;

	@ManyToMany(cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
	private Set<UserRole> roles = new HashSet<UserRole>();
	
	public User() {
		super();
		this.enabled=false;
	}
	
}
