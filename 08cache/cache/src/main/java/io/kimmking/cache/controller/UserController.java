package io.kimmking.cache.controller;

import io.kimmking.cache.entity.User;
import io.kimmking.cache.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@EnableAutoConfiguration
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/user/create")
    public String create(@RequestBody User user) {
        userService.create(user);
        return "ok";
    }
    
    @GetMapping("/user/find")
    public User find(Integer id) {
        return userService.find(id);
    }

    @GetMapping("/user/list")
    public List<User> list() {
        return userService.list();
    }

    @PutMapping("/user/update")
    public String update(@RequestBody User user) {
        userService.update(user);
        return "ok";
    }

    @DeleteMapping("/user/delete")
    public String delete(Integer id) {
        userService.delete(id);
        return "ok";
    }
}