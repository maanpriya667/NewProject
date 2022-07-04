package com.test.demo.validations.validators.request;

import com.test.demo.model.request.UserRequestModel;
import com.test.demo.model.response.ResponseModel;
import com.test.demo.validations.groups.LoginInfoValidationGroup;
import com.test.demo.validations.groups.SignupInfoValidationGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class UserRequestValidator {

    @Autowired
    private Validator validator;
    public ResponseModel<?> validateSignupRequest(UserRequestModel userRequestModel) {
        Set<ConstraintViolation<UserRequestModel>> validate = validator.validate(userRequestModel,
                SignupInfoValidationGroup.class);
        return getResponseOnValidation(validate);
    }

    public ResponseModel<?> validateLoginRequest(UserRequestModel userRequestModel) {
        Set<ConstraintViolation<UserRequestModel>> validate = validator.validate(userRequestModel,
                LoginInfoValidationGroup.class);
        return getResponseOnValidation(validate);
    }


    private ResponseModel<?> getResponseOnValidation(
            Set<ConstraintViolation<UserRequestModel>> validate) {

        if (!validate.isEmpty()) {
            Map<String, String> errors = new HashMap<String, String>();
            for (ConstraintViolation<UserRequestModel> constraintViolation : validate) {
                if (constraintViolation.getPropertyPath().toString() != null
                        && !constraintViolation.getPropertyPath().toString().isEmpty())
                    errors.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
                else {
                    String[] result = constraintViolation.getMessage().split(":");
                    return new ResponseModel<>(HttpStatus.BAD_REQUEST, result[1], null, null);
                }
            }
            return new ResponseModel<>(HttpStatus.BAD_REQUEST, "Validation errors", null, null, errors);
        }
        return null;
    }
}
