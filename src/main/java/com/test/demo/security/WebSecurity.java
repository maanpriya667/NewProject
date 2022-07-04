package com.test.demo.security;

import com.test.demo.exceptions.RestAccessDeniedHandler;
import com.test.demo.exceptions.RestAuthEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private transient final Environment environment;

    @Autowired
    public WebSecurity(final Environment environment) {
        super();
        this.environment = environment;
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll()
                .antMatchers(HttpMethod.GET, environment.getProperty("health.check.ready.path")).permitAll()
                .antMatchers(HttpMethod.POST, "/v1/users/signup").permitAll()
                .antMatchers(HttpMethod.POST, "/v1/users/login").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated().and()
                .addFilter(new AuthFilter(authenticationManager(), environment));
        http.headers().frameOptions().disable();
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler())
                .authenticationEntryPoint(authenticationEntryPoint());
    }

    private RestAccessDeniedHandler accessDeniedHandler() {
        return new RestAccessDeniedHandler();
    }

    private RestAuthEntryPoint authenticationEntryPoint() {
        return new RestAuthEntryPoint();
    }

}
