package com.ecommerce.admin.security;

import com.ecommerce.admin.entity.Customer;
import com.ecommerce.admin.entity.User;
import com.ecommerce.admin.repository.CustomerRepository;
import com.ecommerce.admin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        List.of(new SimpleGrantedAuthority(user.getRole()))
                ))
                .orElseGet(() -> {
                    Customer customer = customerRepository.findByEmail(username)
                            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
                    return new org.springframework.security.core.userdetails.User(
                            customer.getEmail(),
                            customer.getPassword(),
                            List.of(new SimpleGrantedAuthority(customer.getRole()))
                    );
                });
    }
}
