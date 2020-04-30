package com.league.service.user;

import com.league.model.User;
import com.league.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public User findById(int id) {
        return userRepository.findById(id).get();
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public void actualizeUser(User user) {
        User theUser = userRepository.findById(user.getId()).get();
        theUser.setRoles(user.getRoles());
        theUser.setActive(user.isActive());
        userRepository.save(theUser);
    }

    @Override
    public void deleteUser(int userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public List<User> searchUsersByPhrase(String phrase) {
        return userRepository.findUsersByPhrase(phrase);
    }
}
