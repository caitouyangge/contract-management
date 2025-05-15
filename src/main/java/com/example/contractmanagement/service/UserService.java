package com.example.contractmanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.contractmanagement.model.User;
import com.example.contractmanagement.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public boolean register(String username, String password, String email, String role) {
        if (userRepository.findByUsername(username) != null) {
            return false; // 用户已存在
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // 注意：生产项目请加密！
        user.setEmail(email);
        user.setRole(role);

        userRepository.save(user);
        System.out.println("用户已保存：" + user.getUsername());

        return true;
    }
    
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean login(String username, String password) {
        User user = userRepository.findByUsername(username);
        return user != null && user.getPassword().equals(password);
    }
    public User getLoginUser(String username, String password) {
        User user = userRepository.findByUsername(username);
        return user;
    }
    public void save(User user) {
    userRepository.save(user);
    }


    public List<User> findAll() {
        return userRepository.findAll();    
    }

    public List<User> findAssignableUsers() {
        return userRepository.findByAuthorizedTrueAndRoleIn(
            List.of("admin", "operator")
        );
    }
    public boolean validateUserRoles(List<Long> userIds) {
        List<User> users = userRepository.findAllById(userIds);
        return users.stream()
            .allMatch(u -> "admin".equals(u.getRole()) || "operator".equals(u.getRole()));
    }
    
}

