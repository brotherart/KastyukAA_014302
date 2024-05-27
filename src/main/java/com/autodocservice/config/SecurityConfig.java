package com.autodocservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin").password("{noop}password").roles("ADMIN")
                .and()
                .withUser("chief").password("{noop}password").roles("HEAD")
                .and()
                .withUser("manager").password("{noop}password").roles("USER")
                .and()
                .withUser("client").password("{noop}password").roles("WORKER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                .and()
                .authorizeHttpRequests()
                .antMatchers(HttpMethod.GET, "/documents/search").hasRole("WORKER")
                .antMatchers(HttpMethod.GET, "/users/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/documents/{documentId}").hasRole("HEAD")
                .antMatchers(HttpMethod.POST, "/documents/{documentId}/transfer").hasRole("USER")
                .antMatchers(HttpMethod.POST, "/documents/add").hasRole("WORKER")
                .antMatchers(HttpMethod.POST, "/documents/{documentId}/comment").hasRole("WORKER")
                .antMatchers(HttpMethod.PUT, "/documents/{id}/edit").hasRole("WORKER")
                .antMatchers(HttpMethod.DELETE, "/documents/{id}/delete").hasRole("WORKER")
                .and()
                .csrf().disable()
                .formLogin().disable();
    }
}
