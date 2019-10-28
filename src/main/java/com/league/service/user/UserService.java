package com.league.service.user;

import com.league.model.User;

import java.util.List;

public interface UserService {
    void addUser(User user);
    User findByUsername(String username);
    User findById(int id);
    List<User> getAll();
    void actualizeUser(User user);
    void deleteUser(int userId);
}
