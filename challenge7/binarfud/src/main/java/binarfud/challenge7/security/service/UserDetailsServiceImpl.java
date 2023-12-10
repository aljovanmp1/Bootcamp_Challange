package binarfud.challenge7.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import binarfud.challenge7.model.User;
import binarfud.challenge7.service.UserService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserService userService;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUserByToken(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found: "+username));

        return UserDetailsImpl.build(user);
    }
}
