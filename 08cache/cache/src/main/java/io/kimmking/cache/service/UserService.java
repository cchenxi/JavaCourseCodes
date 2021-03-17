package io.kimmking.cache.service;

import io.kimmking.cache.entity.User;
import org.springframework.cache.annotation.CacheConfig;

import java.util.List;

@CacheConfig(cacheNames = "users")
public interface UserService {
    void create(User user);

    User find(int id);

    List<User> list();

    User update(User user);

    void delete(int id);
}
