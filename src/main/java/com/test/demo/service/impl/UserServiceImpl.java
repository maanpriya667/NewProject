package com.test.demo.service.impl;

import com.test.demo.entity.UserEntity;
import com.test.demo.model.request.UserRequestModel;
import com.test.demo.repository.UserRepository;
import com.test.demo.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Value("${auth.token.secret}")
    private String authSecret;

    @Override
    public void saveUser(UserRequestModel userDetails) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(userDetails.getEmail());
        userEntity.setPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));
        userEntity.setUsername(userDetails.getUsername());
        try {
            userRepository.save(userEntity);
        } catch (Exception e) {
        }
    }

    @Override
    public String loginUser(UserRequestModel userRequestModel, MultiValueMap<String, String> headers) {
        UserEntity userEntity = userRepository.findByUsername(userRequestModel.getUsername());
        if (userEntity != null && bCryptPasswordEncoder.matches(userRequestModel.getPassword(), userEntity.getPassword())) {
            return generateToken(headers, userEntity);
        }
        return null;
    }


    private String generateToken(MultiValueMap<String, String> headers, UserEntity entity) {
        final Map<String, Object> claims = new ConcurrentHashMap<>();
        String userAgent = headers.getFirst("user-agent");
        claims.put("user-agent", userAgent);
        claims.put("email", entity.getEmail());
        claims.put("iat", new Date());
        List<String> permissions = new ArrayList<>();
        permissions.add("Users_View & Edit");
        permissions.add("Admin_View & Edit");
        claims.put("permissions", permissions);
        return Jwts.builder().setSubject(entity.getUsername()).addClaims(claims)
                .signWith(SignatureAlgorithm.HS512, authSecret).compact();
    }

}
