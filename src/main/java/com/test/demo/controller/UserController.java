package com.test.demo.controller;

import com.test.demo.model.request.UserRequestModel;
import com.test.demo.model.response.ResponseModel;
import com.test.demo.service.UserService;
import com.test.demo.validations.validators.request.UserRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRequestValidator userRequestValidator;
    
    

    @PostMapping(path = "/signup")
    public ResponseEntity<?> signup(@RequestBody UserRequestModel userRequestModel) {
        ResponseModel<?> responseModel = userRequestValidator.validateSignupRequest(userRequestModel);
        if (responseModel != null)
            return ResponseEntity.badRequest().body(responseModel);
        userService.saveUser(userRequestModel);
        return ResponseEntity.ok(new ResponseModel<String>(200, "User Saved successfully!", null, null));
    }
    
    

    @PostMapping(path = "/login")
    public ResponseEntity<?> signup(@RequestBody UserRequestModel userRequestModel,
                                    @RequestHeader(required = false) MultiValueMap<String, String> headers) {
        ResponseModel responseModel = userRequestValidator.validateLoginRequest(userRequestModel);
        if (responseModel != null)
            return ResponseEntity.badRequest().body(responseModel);
        String token = userService.loginUser(userRequestModel, headers);
        if (token != null)
            return ResponseEntity.ok(new ResponseModel<String>(200, "Login Success!", null, token));
        else
            return ResponseEntity.badRequest().body(new ResponseModel<String>(400, "Invalid User details", null, null));
    }
    
    

    @GetMapping(path = "/check")
    public ResponseEntity<?> signup() {
        return ResponseEntity.ok(new ResponseModel<String>(200, "User is Authenticated", null, null));
    }
}
