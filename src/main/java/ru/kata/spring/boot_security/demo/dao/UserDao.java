package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserDao {
    List<User> getAllUsers();

    void addUser(User user, Set<Integer> roleIds);

    void editUserAndHisRoles(int id, User userDetails, Set<Integer> roleIds);

    void deleteUserById(int id);

    User getUserById(int id);

    boolean isTableUsersEmpty();

    Optional<User> getUserByName(String username);
}
