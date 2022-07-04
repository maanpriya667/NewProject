package com.test.demo.validations;



import com.test.demo.validations.validators.field.EmailValidator;

import java.lang.annotation.*;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
@Documented
public @interface  EmailValidation {

    String message() default "Invalid Email.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
