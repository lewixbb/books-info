package pl.czytajto.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import pl.czytajto.constraint.UniqueMail;
import pl.czytajto.service.UserService;

public class UniqueMailValidator implements ConstraintValidator<UniqueMail, String> {

	@Autowired
	private UserService service;
	
	@Override
	public void initialize(UniqueMail constraintAnnotation) {
		// TODO Auto-generated method stub
		ConstraintValidator.super.initialize(constraintAnnotation);
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		return (!service.ifEmailExist(value));
		
	}

	
	
}
