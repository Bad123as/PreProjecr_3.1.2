package com.example.springboot.controller;

import com.example.springboot.model.Role;
import com.example.springboot.service.RoleService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.springboot.model.User;
import com.example.springboot.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Controller
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/")
    public String showAllUser(Model model, @ModelAttribute("users") User user) {
        model.addAttribute("users", userService.getAllUsers());
        return "redirect:/login";
    }

    @GetMapping("/admin")
    public String getAdminPage(Model model, @ModelAttribute("users") User user) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getAllRoles());
        return "admin";
    }

    @GetMapping("/login")
    public String getLogin(Model model, @ModelAttribute("users") User user, @ModelAttribute("email") String email, @ModelAttribute("password") String password) {
        return "login";
    }

    @GetMapping("/logout")
    public String getLogout(Model model, @ModelAttribute User user) {
        return "redirect:/login";
    }

    @GetMapping(value = "/user")
    public String userInfo(@ModelAttribute User user, Model model, @ModelAttribute("users") User users){
        model.addAttribute("user", user);
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("roles", roleService.getAllRoles());
        return "users";
    }

    @GetMapping(value = "/admin/new")
    public String createNewUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.getAllRoles());
        return "new";
    }

    @PostMapping(value = "/admin/save-user")
    public String addNewUser(@ModelAttribute User user, @RequestParam(value = "checkBoxRoles") String[] checkBoxRoles) {
        Set<Role> roleSet = new HashSet<>();
        for (String role : checkBoxRoles) {
            roleSet.add(roleService.getRoleByName(role));
        }
        user.setRoles(roleSet);
        userService.addUser(user);

        return "redirect:/admin";
    }

    @PostMapping(value = "/admin/edit/{id}")
    public String updateUser(@ModelAttribute("id") long id, @ModelAttribute User user, @RequestParam(value = "checkBoxRoless") String[] checkBoxRoless) {
        Set<Role> roleSet = new HashSet<>();
        for (String role : checkBoxRoless) {
            roleSet.add(roleService.getRoleByName(role));
        }
        user.setRoles(roleSet);
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/admin/{id}/delete")
    public String delete(@ModelAttribute("id") long id, @ModelAttribute("users") User user) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}