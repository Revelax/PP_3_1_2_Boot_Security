package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }


    @Override
    public List<User> getAllPeople() {
        return userDao.getAllUsers();
    }

    @Override
    @Transactional
    public void addUser(User user, Set<Integer> roleIds) {
        userDao.addUser(user, roleIds);
    }

    @Override
    @Transactional
    public void deleteUserById(int id) {
        userDao.deleteUserById(id);
    }

    @Override
    @Transactional
    public void editUserAndHisRoles(int id, User userDetails, Set<Integer> roleIds) {
        userDao.editUserAndHisRoles(id, userDetails, roleIds);
    }

    @Override
    public User getUserById(int id) {
        return userDao.getUserById(id);
    }

    @Override
    public Optional<User> getUserByName(String username) {
        return userDao.getUserByName(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userDao.getUserByName(username);

        if (user.isEmpty())
            throw new UsernameNotFoundException("Пользователь не найден");

        return user.get();
    }

    @Override
    public boolean isTableUsersEmpty() {
        return userDao.isTableUsersEmpty();
    }
}
