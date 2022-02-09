package com.example.app.servlets;


import com.example.app.auth.UserEntityDetailsService;
import com.example.app.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private UserEntityDetailsService userEntityDetailsService;



    private DataSource dataSource;
    private UserRepo userRepo;

    public WebSecurityConfig(final DataSource dataSource, final UserRepo userRepo) {
        this.dataSource = dataSource;
        this.userRepo = userRepo;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println("configure http");
        http.authorizeRequests()
                    .antMatchers("/reg").permitAll()
                    .antMatchers("/homepage").hasAnyRole("USER","ADMIN")
                    .antMatchers("homepage/post/23").hasAnyRole("USER","ADMIN")
                    .antMatchers("/blog/add").hasAnyRole("USER","ADMIN")
                    .antMatchers(HttpMethod.POST, "/homepage/post").permitAll()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .successForwardUrl("/homepage")
                    .failureUrl("/login?error=true")
                    .permitAll()
                .and()
                    .logout()
                    .permitAll()
                .and()
                    .httpBasic()
                .and()
                .csrf().disable();


    }


    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        System.out.println("configure auth");
        auth.userDetailsService(new UserEntityDetailsService(userRepo));
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userEntityDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}