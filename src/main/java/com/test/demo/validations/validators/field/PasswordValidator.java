package com.test.demo.validations.validators.field;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.test.demo.validations.PasswordValidation;

public class PasswordValidator implements ConstraintValidator<PasswordValidation, String>{

	@Override
	public boolean isValid(String password, ConstraintValidatorContext context) {
		
		 return Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$")
         .matcher(password)
         .matches();
	}
	
	

}
