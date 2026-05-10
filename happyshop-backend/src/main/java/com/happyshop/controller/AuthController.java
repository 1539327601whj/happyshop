package com.happyshop.controller;

import com.happyshop.common.Result;
import com.happyshop.dto.LoginDTO;
import com.happyshop.dto.RegisterDTO;
import com.happyshop.entity.User;
import com.happyshop.service.UserService;
import com.happyshop.vo.LoginVO;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Result<?> register(@Valid @RequestBody RegisterDTO dto) {
        userService.register(dto.getUsername(), dto.getPassword(), dto.getNickname(), dto.getPhone());
        return Result.success();
    }

    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO dto) {
        String token = userService.login(dto.getUsername(), dto.getPassword());
        User user = userService.getUserByUsername(dto.getUsername());

        LoginVO vo = new LoginVO();
        vo.setToken(token);
        vo.setUser(user);
        return Result.success(vo);
    }

    @GetMapping("/info")
    public Result<User> getUserInfo() {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Result.success(userService.getUserById(userId));
    }

    @PutMapping("/info")
    public Result<?> updateUserInfo(@RequestBody User user) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.updateUserInfo(userId, user);
        return Result.success();
    }

    @PutMapping("/password")
    public Result<?> changePassword(@RequestBody java.util.Map<String, String> params) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.changePassword(userId, params.get("oldPassword"), params.get("newPassword"));
        return Result.success();
    }
}
