package com.example.app.auth;


import com.example.app.models.UserEntity;
import com.example.app.repo.UserRepo;
import com.example.app.servlets.WebSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserEntityDetailsService implements UserDetailsService {
    final private UserRepo userRepo;

    public UserEntityDetailsService(final UserRepo userRepo) {

        this.userRepo = userRepo;
    }
    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        try {
            UserEntity candidate = userRepo.findByEmail(email);

            if (candidate!= null) {
                if(candidate.getEmail().equals("you@example.com")){
                    return User.withUsername(candidate.getEmail())
                            .password(candidate.getPassword())
                            .roles("ADMIN")
                            .build();
                }
                else {
                    return User.withUsername(candidate.getEmail())
                            .password(candidate.getPassword())
                            .roles("USER")
                            .build();
                }
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        throw new UsernameNotFoundException(email);
    }
}
