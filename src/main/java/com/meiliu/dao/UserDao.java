package com.meiliu.dao;

import com.meiliu.entity.User;

public interface UserDao {

    User findByUserName(String username);

    void save(User user);

}
