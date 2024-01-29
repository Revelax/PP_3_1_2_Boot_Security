package ru.kata.spring.boot_security.demo.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Set;


@Component
public class InitDataUtil {

    private final UserService userService;

    @Autowired
    public InitDataUtil(UserServiceImpl personService) {
        this.userService = personService;
    }

    @PostConstruct
    public void init() {

        if (userService.isTableUsersEmpty()) {
            User user1 = new User();
            user1.setUsername("admin");
            user1.setLastName("admin");
            user1.setAge(30);
            user1.setEmail("admin@mail.ru");
            user1.setPassword("admin");

            User user2 = new User();
            user2.setUsername("user");
            user2.setLastName("user");
            user2.setAge(30);
            user2.setEmail("user@mail.ru");
            user2.setPassword("user");

            Set<Integer> adminR = Collections.singleton(2);
            Set<Integer> userR = Collections.singleton(1);

            userService.addUser(user1, adminR);
            userService.addUser(user2, userR);
        }
    }
}


