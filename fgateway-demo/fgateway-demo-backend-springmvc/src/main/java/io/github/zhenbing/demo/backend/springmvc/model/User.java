package io.github.zhenbing.demo.backend.springmvc.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Description
 * @Author fzb
 * @date 2021.05.23 20:54
 */
@Data
@AllArgsConstructor
public class User {
    private Integer id;
    private String name;
}
