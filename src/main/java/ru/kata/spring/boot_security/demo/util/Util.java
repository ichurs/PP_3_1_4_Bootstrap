package ru.kata.spring.boot_security.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UsersService;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class Util {

    private final RoleService roleService;
    private final UsersService usersService;

    @Autowired
    public Util(RoleService roleService, UsersService usersService) {
        this.roleService = roleService;
        this.usersService = usersService;
    }

    @PostConstruct
    public void initialization() {
        Role roleAdmin = new Role("ROLE_ADMIN");
        Role roleUser = new Role("ROLE_USER");
        roleService.saveRole(roleAdmin);
        roleService.saveRole(roleUser);
        User admin = new User("admin", "admin", "admin", "admin", Set.of(roleAdmin, roleUser));
        usersService.saveUser(admin);

        roleService.saveRole(roleUser);
        User user = new User("user","user", "user", "user", Set.of(roleUser));
        usersService.saveUser(user);

    }
}
