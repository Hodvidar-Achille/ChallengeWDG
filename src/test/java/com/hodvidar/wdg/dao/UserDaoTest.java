package com.hodvidar.wdg.dao;

import com.hodvidar.wdg.dao.api.UserDao;
import com.hodvidar.wdg.dao.mock.UserDaoMock;
import com.hodvidar.wdg.model.User;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserDaoTest {

    UserDao userDao;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoMock();
    }

    @Test
    void when_userDao_saveAnUser_getUserReturnsTheSameObject() {
        final int id1 = 1;
        final int id2 = 2;
        final User u1 = new User(1, BigDecimal.ZERO);
        final User u2 = new User(2, BigDecimal.ZERO);
        userDao.saveUser(u1);
        userDao.saveUser(u2);
        assertThat(userDao.getUser(id1)).isEqualToComparingFieldByField(u1);
        assertThat(userDao.getUser(id2)).isEqualToComparingFieldByField(u2);
    }

    @Test
    void when_userDao_saveAlreadyExistingId_thenMeet_IllegalStateException() {
        final User u1 = new User(1, BigDecimal.ZERO);
        final User u2 = new User(1, BigDecimal.ZERO);
        userDao.saveUser(u1);
        final Throwable exception = assertThrows(IllegalStateException.class,
                () -> userDao.saveUser(u2));
        assertEquals("User already exist for this id '1'", exception.getMessage());
    }

    @Test
    void when_userDao_useWrongId_thenMeet_NotFoundException() {
        final Throwable exception = assertThrows(ResourceNotFoundException.class,
                () -> userDao.getUser(100));
        assertEquals("No user for id '100'", exception.getMessage());
    }
}
