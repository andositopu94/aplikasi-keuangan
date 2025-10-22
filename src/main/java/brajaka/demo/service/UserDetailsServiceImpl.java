package brajaka.demo.service;

import brajaka.demo.config.UserRole;
import brajaka.demo.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
    private static final Map<String, User> users = new HashMap<>();

    static {
        users.put("admin", new User("admin", "admin123", UserRole.ADMIN));
        users.put("supervisor", new User("supervisor", "supervisor123", UserRole.SUPERVISI));
        users.put("user", new User("user", "{noop}user123", UserRole.USER));
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = users.get(username);
        if (user == null) {
            throw new RuntimeException("User not found: " + username);
        }
        return user;
    }
}
