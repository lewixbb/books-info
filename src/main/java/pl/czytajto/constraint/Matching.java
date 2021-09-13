package pl.czytajto.constraint;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.*;

import javax.validation.Constraint;
import javax.validation.Payload;

import pl.czytajto.validation.MatchingPasswordComparator;

@Documented
@Constraint(validatedBy = MatchingPasswordComparator.class)
@Target({FIELD,CONSTRUCTOR,PARAMETER,METHOD,TYPE,ANNOTATION_TYPE})
@Retention(RUNTIME)
public @interface Matching {
	String message() default "blabla";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default{};
	
	String first();
	String second();
	
	@Target({TYPE,ANNOTATION_TYPE})
	@Retention(RUNTIME)
	@Documented
	@interface List
	{
		Matching[] value();
	}
	
}
