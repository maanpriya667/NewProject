package com.test.demo.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.test.demo.validations.EmailValidation;
import com.test.demo.validations.groups.LoginInfoValidationGroup;
import com.test.demo.validations.groups.SignupInfoValidationGroup;

import javax.validation.constraints.NotEmpty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRequestModel {

    @EmailValidation(groups = SignupInfoValidationGroup.class)
    private String email;

    @NotEmpty(message = "Password must not be empty.", groups = {SignupInfoValidationGroup.class, LoginInfoValidationGroup.class})
    private String password;

    @NotEmpty(message = "Username must not be empty.", groups = {SignupInfoValidationGroup.class, LoginInfoValidationGroup.class})
    private String username;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
