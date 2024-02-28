package com.autodocservice.config;

import com.autodocservice.service.UsersService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin").password("{noop}password").roles("ADMIN")
                .and()
                .withUser("chief").password("{noop}password").roles("CHIEF")
                .and()
                .withUser("manager").password("{noop}password").roles("MANAGER")
                .and()
                .withUser("client").password("{noop}password").roles("CLIENT")
        ;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                .and()
                .authorizeHttpRequests()
                .antMatchers(HttpMethod.GET, "/users/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/documents/{documentId}").hasRole("CHIEF")
                .antMatchers(HttpMethod.POST, "/documents/{documentId}/transfer").hasRole("MANAGER")
                .antMatchers(HttpMethod.POST, "/documents/add").hasRole("CLIENT")
                .antMatchers(HttpMethod.DELETE, "/documents/{id}/delete").hasRole("CLIENT")
                .antMatchers(HttpMethod.PATCH, "/documents/{id}/edit").hasRole("CLIENT")
                .antMatchers(HttpMethod.POST, "/documents/{documentId}/comment").hasRole("CLIENT")
                .and()
                .csrf().disable()
                .formLogin().disable()
        ;
    }
}
