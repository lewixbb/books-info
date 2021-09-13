package pl.czytajto.constraint;

import javax.validation.Constraint;
import javax.validation.Payload;

import pl.czytajto.validation.UniqueMailValidator;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = UniqueMailValidator.class)
@Target({FIELD,METHOD,PARAMETER,CONSTRUCTOR,ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueMail {

	String message() default "niestety podany adres email juz egzystuje w naszym serwisie, proszę podaj inny adres email";
	Class<?>[] groups() default{};
	Class<? extends Payload>[] payload() default{};
	
	
}
