package io.github.zhenbing.demo.backend.springmvc.service;

import io.github.zhenbing.demo.backend.springmvc.model.User;

/**
 * @Description
 * @Author fzb
 * @date 2021.05.23 20:55
 */
public interface UserService {
    User findById(Integer id);
}
