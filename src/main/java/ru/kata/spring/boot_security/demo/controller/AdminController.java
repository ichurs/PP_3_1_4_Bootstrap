package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UsersService;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UsersService usersService;
    private final RoleService roleService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(UsersService usersService, RoleService roleService,
                           BCryptPasswordEncoder passwordEncoder) {
        this.usersService = usersService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String getAdminPage(Model model) {
        model.addAttribute("users", usersService.getAllUsers());
        return "/admin";
    }

    @GetMapping("/new")
    public String getUserCreateForm(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("roles", roleService.getRoles());
        return "new";
    }

    @PostMapping("/createNew")
    public String createUser(@ModelAttribute("user") User user,
                             @RequestParam(value = "stringRole") String stringRole) {
        Role role = new Role(stringRole);
        roleService.saveRole(role);
        user.setRoles(Set.of(role));
        usersService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping(value = "/{id}/edit")
    public String getUserEditForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", usersService.getUserById(id));
        model.addAttribute("roles", roleService.getRoles());
        return "edit";
    }

    @PatchMapping(value = "/{id}")
    public String updateUser(@ModelAttribute("user") User user, @RequestParam(value = "stringRole") String stringRole) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = new Role(stringRole);
        roleService.saveRole(role);
        user.setRoles(Set.of(role));
        usersService.updateUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}/delete")
    public String removeUserById(@PathVariable("id") Long id) {
        usersService.removeUserById(id);
        return "redirect:/admin";
    }
}
