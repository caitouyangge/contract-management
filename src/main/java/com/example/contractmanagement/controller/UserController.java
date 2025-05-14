package com.example.contractmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.contractmanagement.dto.ApiResponse;
import com.example.contractmanagement.model.User;
import com.example.contractmanagement.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;
    @GetMapping("/find")
    public List<User> getAllUsers() {
        return userService.findAll();
    }
    @PutMapping("/{username}/role")
public ResponseEntity<String> updateUserRole(
        @PathVariable String username,
        @RequestBody RoleUpdateRequest request
) {
    // 用 password=null 调用 getLoginUser 是为了复用你已有的查询逻辑
    User user = userService.getLoginUser(username, null);

    if (user == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("用户不存在");
    }

    user.setRole(request.getRole());
    userService.save(user); // 假设你在 UserService 中已有 save 方法，如果没有我也会告诉你怎么加

    return ResponseEntity.ok("用户权限更新成功");
}

    

    public static class RoleUpdateRequest {
        private String role;

        public String getRole() {
            return role;
        }
        public void setRole(String role) {
            this.role = role;
        }
    }

    @PostMapping("/register")
    public String register(@RequestBody UserDTO user) {
        boolean success = userService.register(
            user.getUsername(),
            user.getPassword(),
            user.getEmail(),
            user.getRole()
        );
        
        return success ? "注册成功" : "用户名已存在";
    }

    @PostMapping("/login")
public ApiResponse<User> login(@RequestBody UserDTO user) {
    boolean success = userService.login(user.getUsername(), user.getPassword());

    if (!success) {
        return ApiResponse.error("用户名或密码错误");
    }

    User foundUser = userService.getLoginUser(user.getUsername(), user.getPassword());
    return ApiResponse.success(foundUser);
}
    
    @GetMapping("/assignable-users")
    public ResponseEntity<List<User>> getAssignableUsers() {
        List<User> users = userService.findAssignableUsers();
        return ResponseEntity.ok(users);
    }


    public static class UserDTO {
        private String username;
        private String password;
        private String email;
        private String role;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }
}
