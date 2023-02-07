package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UsersService;

import java.security.Principal;
import java.util.Set;


@Controller
@RequestMapping()
public class UsersController {

    private final UsersService usersService;
    private final RoleService roleService;

    @Autowired
    public UsersController(UsersService usersService, RoleService roleService) {
        this.usersService = usersService;
        this.roleService = roleService;
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("user") User user) {
        return "registration_user";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("user") User user) {
        Role role = new Role("ROLE_USER");
        roleService.saveRole(role);
        user.setRoles(Set.of(role));
        usersService.saveUser(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/user")
    public String getUserPage(Model model, Principal principal) {
        Long id = usersService.getUserByUsername(principal.getName()).getId();
        model.addAttribute("user", usersService.getUserById(id));
        return "show_user";
    }
}
