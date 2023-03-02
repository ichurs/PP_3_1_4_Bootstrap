package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UsersService;

import javax.validation.Valid;
import java.util.List;

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
    public String getUserCreateForm(Model model) {
        User user = new User();
        List<Role> roles = roleService.getRoles();
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        return "new";
    }

    @PostMapping("/createNew")
    public String createUser(ModelMap model, @ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<Role> roles = roleService.getRoles();
            model.addAttribute("roles", roles);
            return "new";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        usersService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping(value = "edit/{id}")
    public String editUser(@PathVariable("id") Long id, Model model) {
        User user = usersService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getRoles());
        return "edit";
    }

    @PatchMapping(value = "edit/{id}")
    public String updateUser(ModelMap model, @ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                             @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            List<Role> roles = roleService.getRoles();
            model.addAttribute("roles", roles);
            return "edit";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        usersService.updateUser(user, id);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}/delete")
    public String removeUserById(@PathVariable("id") Long id) {
        usersService.removeUserById(id);
        return "redirect:/admin";
    }
}
