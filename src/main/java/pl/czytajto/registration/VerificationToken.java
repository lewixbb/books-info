package pl.czytajto.registration;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.engine.jdbc.SerializableBlobProxy;

import lombok.Data;
import pl.czytajto.user.model.User;

@Entity
public @Data class VerificationToken implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private static final int EXPRIATION = 60*24;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String token;
	@OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false)
	private User user;
	
	private Date expiryDate;
	
	private Date calculateExpiryDate (int expiryTimeInMinute) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Timestamp(cal.getTime().getTime()));
		cal.add(Calendar.MINUTE, expiryTimeInMinute);
		return new Date(cal.getTime().getTime());
	}
	
	public VerificationToken() {}

	public VerificationToken(String token, User user) {
		super();
		this.token = token;
		this.user = user;
		this.expiryDate = calculateExpiryDate(EXPRIATION);
	}
	
	
	
	
	
	
}
