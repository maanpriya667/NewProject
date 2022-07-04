package com.test.demo.service;

import com.test.demo.model.request.UserRequestModel;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;


public interface UserService {

    void saveUser(UserRequestModel userDetails);

    String loginUser(UserRequestModel userRequestModel, MultiValueMap<String, String> headers);

}
