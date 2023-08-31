package com.jd.dev.app.auth.services;

import com.jd.dev.app.auth.clients.UserFeignClient;
import com.jd.dev.app.user.commons.models.entity.User;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {
    private final UserFeignClient client;
    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserFeignClient client) {
        this.client = client;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = client.findByUsername(username);

            List<SimpleGrantedAuthority> roles = user.getRoles().stream()
                    .map(rol -> new SimpleGrantedAuthority(rol.getName()))
                    .peek(authority -> log.info("Role : " + authority.getAuthority()))
                    .collect(Collectors.toList());

            log.info("Logged user: " + username);

            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.isEnabled(),
                    true, true, true, roles);
        } catch (FeignException feignException) {
            throw new UsernameNotFoundException("User not found");
        }

    }

    @Override
    public User findByUsername(String username) {
        return client.findByUsername(username);
    }

    @Override
    public User update(User user, Long id) {
        return client.update(user, id);
    }
}
