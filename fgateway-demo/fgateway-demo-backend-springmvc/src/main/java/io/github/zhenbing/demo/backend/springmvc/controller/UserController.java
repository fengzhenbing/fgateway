package io.github.zhenbing.demo.backend.springmvc.controller;

import io.github.zhenbing.demo.backend.springmvc.model.User;
import io.github.zhenbing.demo.backend.springmvc.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Author fzb
 * @date 2021.05.23 20:52
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public User findUser(@PathVariable("id") Integer id) {
        return userService.findById(id);
    }
}
