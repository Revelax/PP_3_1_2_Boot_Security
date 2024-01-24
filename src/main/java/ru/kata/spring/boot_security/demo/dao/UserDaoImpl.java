package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static ru.kata.spring.boot_security.demo.configs.WebSecurityConfig.passwordEncoder;

@Repository
public class UserDaoImpl implements UserDao {

    private final RoleService roleService;

    public UserDaoImpl(RoleService roleService) {
        this.roleService = roleService;
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean isTableUsersEmpty() {
        Query query = entityManager.createQuery("SELECT COUNT(*) FROM User ");
        Long count = (Long) query.getSingleResult();
        return count == 0;
    }

    @Override
    public List<User> getAllUsers() {
        return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
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
        user.setPassword(passwordEncoder().encode(user.getPassword()));
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
                user.setPassword(passwordEncoder().encode(userDetails.getPassword()));
            }
        } else {
            throw new RuntimeException("Пользователь с таким id не найден");
        }
    }

    @Override
    public void deleteUserById(int id) {
        entityManager.remove(entityManager.find(User.class, id));
    }

    @Override
    public Optional<User> getUserByName(String username) {
        String query = "SELECT users FROM User users WHERE users.username = :username";
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
        return entityManager.find(User.class, id);
    }
}
