package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final UserValidator userValidator;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, UserValidator userValidator, RoleService roleService) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.roleService = roleService;
    }

    @GetMapping("")
    public String adminPage(Model model) {
        model.addAttribute("user", userService.getAllPeople());
        return "/hello/admin";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable("id") int id, Model model) {
        model.addAttribute("editUser", userService.getUserById(id));
        model.addAttribute("roles", roleService.getAllRoles());
        return "/admin/edit";
    }

    @PatchMapping("/edit/{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("editUser") @Valid User updateUser, BindingResult bindingResult,
                         @RequestParam(value = "roles", required = false) Set<Integer> roleIds) {
        User user = userService.getUserById(id);
        if (!user.getUsername().equals(updateUser.getUsername())) {
            userValidator.validate(updateUser, bindingResult);
        }
        if (bindingResult.hasErrors())
            return "/admin/edit";

        userService.editUserAndHisRoles(id, updateUser, roleIds);
        return "redirect:/admin";
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id) {
        userService.deleteUserById(id);
        return "redirect:/admin";
    }

    @GetMapping("/add")
    public String registrationPage(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("roles", roleService.getAllRoles());
        return "/admin/add";
    }

    @PostMapping("/add")
    public String performRegistration(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                                      @RequestParam(value = "roles", required = false) Set<Integer> roleIds) {
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors())
            return "/admin/add";

        userService.addUser(user, roleIds);
        return "redirect:/admin";
    }
}

