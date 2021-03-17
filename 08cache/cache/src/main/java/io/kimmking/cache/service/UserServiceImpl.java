package io.kimmking.cache.service;

import io.kimmking.cache.entity.User;
import io.kimmking.cache.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public void create(User user) {
        userMapper.create(user);
    }

    // 开启spring cache
    @Cacheable(key="#id", value="userCache", unless = "#result==null")
    @Override
    public User find(int id) {
        System.out.println(" ==> find " + id);
        return userMapper.find(id);
    }

    // 开启spring cache
    @Cacheable(key="'list'", value="userCache")
    @Override
    public List<User> list(){
        return userMapper.list();
    }

    @Override
    @Caching(
            put = {@CachePut(value = "userCache", key = "#user.id")},
            evict = {@CacheEvict(value = "userCache", key = "'list'")}
    )
    public User update(User user) {
        userMapper.update(user);
        return user;
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "userCache", key = "#id"),
                    @CacheEvict(value = "userCache", key = "'list'")
            }
    )
    public void delete(int id) {
        userMapper.delete(id);
    }
}
