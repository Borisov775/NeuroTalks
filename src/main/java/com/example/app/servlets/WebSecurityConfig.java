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
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import com.example.app.auth.TwoFactorFilter;
import com.example.app.auth.TotpAuthenticationProvider;
import com.example.app.auth.TotpService;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private UserEntityDetailsService userEntityDetailsService;

    private DataSource dataSource;
    private UserRepo userRepo;
    private TotpService totpService;

    public WebSecurityConfig(final DataSource dataSource, final UserRepo userRepo, final TotpService totpService) {
        this.dataSource = dataSource;
        this.userRepo = userRepo;
        this.totpService = totpService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println("configure http");

        http.authorizeRequests()
            .antMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/swagger-resources/**").permitAll()
            .antMatchers("/reg").permitAll()
            .antMatchers("/2fa", "/2fa/**").permitAll()
            .antMatchers("/homepage").hasAnyRole("USER", "ADMIN")
            .antMatchers("/homepage/**").hasAnyRole("USER", "ADMIN")
            .antMatchers("/blog/add").hasAnyRole("USER", "ADMIN")
            .antMatchers(HttpMethod.POST, "/homepage/post").permitAll();

        // Require authentication for any other request (redirects to /login)
        http.authorizeRequests().anyRequest().authenticated();

        http.formLogin()
                .loginPage("/login")
                .successForwardUrl("/homepage")
                .failureUrl("/login?error=true")
                .permitAll();

        http.logout()
                .permitAll();

        http.addFilterBefore(new TwoFactorFilter(userRepo), SecurityContextPersistenceFilter.class);

        http.httpBasic();

        http.csrf().disable();


    }


    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        System.out.println("configure auth");
        // Register TOTP provider first so login-by-code is attempted before password checks
        TotpAuthenticationProvider totpProvider = new TotpAuthenticationProvider(userRepo, totpService, userEntityDetailsService);
        auth.authenticationProvider(totpProvider);

        // Register DAO authentication provider for standard username/password
        DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
        daoProvider.setUserDetailsService(userEntityDetailsService);
        daoProvider.setPasswordEncoder(passwordEncoder());
        auth.authenticationProvider(daoProvider);
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