package brajaka.demo.service;

import brajaka.demo.config.UserRole;
import brajaka.demo.model.UserEntity;
import brajaka.demo.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

    private  final UserRepository userRepository;
    private  final PasswordEncoder passwordEncoder;

    public UserDetailsServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
//    private static final Map<String, User> users = new HashMap<>();
//
//    static {
//        users.put("admin", new User("admin", "admin123", UserRole.ADMIN));
//        users.put("supervisor", new User("supervisor", "supervisor123", UserRole.SUPERVISI));
//        users.put("user", new User("user", "{noop}user123", UserRole.USER));
//    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = users.get(username);
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
//        if (user == null) {
//            throw new RuntimeException("User not found: " + username);
//        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRole().name())
                .build();

//        return user;
    }

    public void registerUser(String username, String rawPassword, String roleStr) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists: " + username);
        }
        UserRole role;
        try {
            role = UserRole.valueOf(roleStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role: " + roleStr);
        }

        UserEntity newUser = new UserEntity();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(rawPassword));
        newUser.setRole(role);

        userRepository.save(newUser);
    }
}
