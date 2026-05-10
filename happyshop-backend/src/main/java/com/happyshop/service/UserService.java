package com.happyshop.service;

import com.happyshop.entity.User;

public interface UserService {

    User register(String username, String password, String nickname, String phone);

    String login(String username, String password);

    User getUserById(Long id);

    User getUserByUsername(String username);

    void updateUserInfo(Long userId, User user);

    void changePassword(Long userId, String oldPassword, String newPassword);
}
