package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService extends UserDetailsService {
    List<User> getAllPeople();

    void addUser(User user, Set<Integer> roleIds);

    void deleteUserById(int id);

    void editUserAndHisRoles(int id, User userDetails, Set<Integer> roleIds);

    User getUserById(int id);
    Optional<User> getUserByName(String username);

    boolean isTableUsersEmpty();

}
