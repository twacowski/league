package com.league.service.user;

import com.league.model.User;
import com.league.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Override
    public void addUser(User user) {

        user.setActive(true);
        user.setRoles("ROLE_USER");
        String password = passwordEncoder.encode(user.getPassword());
        user.setPassword(password);

        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUserName(username).get();
    }
}
