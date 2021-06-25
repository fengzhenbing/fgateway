package io.github.zhenbing.demo.backend.springmvc.service.impl;

import io.github.zhenbing.demo.backend.springmvc.model.User;
import io.github.zhenbing.demo.backend.springmvc.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author fzb
 * @date 2021.05.23 20:55
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Override
    public User findById(Integer id) {
        User user = new User(id, "fzb" + id);
        log.info("find user:{}", user);
        return user;
    }
}
