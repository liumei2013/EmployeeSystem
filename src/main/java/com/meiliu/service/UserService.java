package com.meiliu.service;

import com.meiliu.entity.User;

public interface UserService {

    public abstract void register(User user);

    public abstract User login(String username, String password);
}
