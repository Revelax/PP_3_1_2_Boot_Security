package ru.kata.spring.boot_security.demo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class UserDaoImpl implements UserDao {

    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    public UserDaoImpl(RoleService roleService, PasswordEncoder passwordEncoder) {
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public boolean isTableUsersEmpty() {
        Query query = entityManager.createQuery("SELECT COUNT(*) FROM User ");
        Long count = (Long) query.getSingleResult();
        return count == 0;
    }

    @Override
    public List<User> getAllUsers() {
        return entityManager.createQuery("SELECT u FROM User u ", User.class).getResultList();
    }

    @Override
    public void addUser(User user, Set<Integer> roleIds) {
        if (roleIds != null) {
            user.setRoles(roleService.getRolesByIds(roleIds));
        } else {
            Set<Integer> set = new HashSet<>();
            set.add(1);
            user.setRoles(roleService.getRolesByIds(set));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        entityManager.persist(user);
    }

    @Override
    public void editUserAndHisRoles(int id, User userDetails, Set<Integer> roleId) {
        User user = entityManager.find(User.class, id);
        if (user != null) {
            user.setUsername(userDetails.getUsername());
            user.setLastName(userDetails.getLastName());
            user.setAge(userDetails.getAge());
            user.setEmail(userDetails.getEmail());
            if (roleId != null) {
                Set<Role> selectedRole = roleService.getRolesByIds(roleId);
                user.setRoles(selectedRole);
            }
            if (!userDetails.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
            }
        } else {
            throw new EntityNotFoundException("Пользователь с таким id не найден");
        }
    }

    @Override
    public void deleteUserById(int id) {
        User user = entityManager.find(User.class, id);
        if (user == null) {
            throw new EntityNotFoundException("Пользователь с таким id не найден");
        }
        entityManager.remove(user);
    }

    @Override
    public Optional<User> getUserByName(String username) {
        String query = "SELECT users FROM User users JOIN FETCH users.roles WHERE users.username = :username";
        try {
            User user = entityManager.createQuery(query, User.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public User getUserById(int id) {
        User user = entityManager.find(User.class, id);
        if (user == null) {
            throw new EntityNotFoundException("Пользователь с таким id не найден");
        }
        return user;
    }
}
