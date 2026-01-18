package com.examly.springapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
 
import com.examly.springapp.model.User;
import com.examly.springapp.repository.UserRepo;
 
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    UserRepo repo;
 
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User existing = repo.findByUsername(username);
 
        if(existing == null ){
            throw new UsernameNotFoundException("Username Not Found");
        }
 
        return new UserPrinciple(existing);
    }
}