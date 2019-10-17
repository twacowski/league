package com.league.service.user;

import com.league.model.User;

public interface UserService {
    void addUser(User user);
    User findByUsername(String username);
}
