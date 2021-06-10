package com.hodvidar.wdg.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String BASE_URL = "/wdg/api/rest/**";
    private static final String USER = "USER";

    // Create 2 users for demo
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication()
                .withUser("user").password("{noop}password").roles(USER)
                .and()
                .withUser("admin").password("{noop}password").roles("USER", "ADMIN");

    }

    // Secure the endpoints with HTTP Basic authentication
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            //HTTP Basic authentication
            .httpBasic()
            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.GET, BASE_URL).hasRole(USER)
            .antMatchers(HttpMethod.POST, BASE_URL).hasRole(USER)
            .antMatchers(HttpMethod.PUT, BASE_URL).hasRole(USER)
            .antMatchers(HttpMethod.PATCH, BASE_URL).hasRole(USER)
            .antMatchers(HttpMethod.DELETE, BASE_URL).hasRole(USER)
            .and()
            .csrf().disable()
            .formLogin().disable();
    }

    // from https://mkyong.com/spring-boot/spring-rest-spring-security-example/
    // could improve security using this instead:
    // https://octoperf.com/blog/2018/03/08/securing-rest-api-spring-security/#architecture
}
