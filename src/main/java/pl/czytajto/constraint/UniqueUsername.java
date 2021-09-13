package pl.czytajto.constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

import javax.validation.Constraint;
import javax.validation.Payload;

import pl.czytajto.validation.UniqueUsernameValidator;

@Documented
@Constraint(validatedBy = UniqueUsernameValidator.class)
@Target({FIELD, CONSTRUCTOR ,METHOD ,PARAMETER, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueUsername {
	String message() default "niestety podana nazwa użytkownika juz egzystuje w naszym serwisie, proszę podaj inną nazwę użytkownika";
	Class <?>[] groups() default {};
	Class <? extends Payload>[] payload() default{};	
}
