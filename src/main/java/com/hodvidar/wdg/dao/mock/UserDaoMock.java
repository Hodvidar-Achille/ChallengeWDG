package com.hodvidar.wdg.dao.mock;

import com.hodvidar.wdg.dao.api.UserDao;
import com.hodvidar.wdg.model.User;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserDaoMock implements UserDao {

    private final Map<Integer, User> companies;

    @Autowired
    public UserDaoMock() {
        this.companies = new HashMap<>();
    }


    @Override
    public User getUser(int userId) {
        User user = this.companies.get(userId);
        if (user == null) {
            throw new ResourceNotFoundException("No user for id '" + userId + "'");
        }
        return user;
    }

    @Override
    public void saveUser(User user) {
        if (companies.containsKey(user.getId())) {
            throw new IllegalStateException("User already exist for this id '" + user.getId() + "'");
        }
        this.companies.put(user.getId(), user);
    }
}