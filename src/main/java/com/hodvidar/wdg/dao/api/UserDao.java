package com.hodvidar.wdg.dao.api;

import com.hodvidar.wdg.model.User;

public interface UserDao {

    User getUser(int userId);

    void saveUser(User user);
}
