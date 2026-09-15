package com.lankafreshmart.lanka_fresh_mart.controller;

import com.lankafreshmart.lanka_fresh_mart.dto.UserDto;
import com.lankafreshmart.lanka_fresh_mart.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @PostMapping("/fast-switch")
    public String fastSwitch(@org.springframework.web.bind.annotation.RequestParam String email, jakarta.servlet.http.HttpServletRequest request) {
        try {
            request.logout();
            request.login(email, "password123");
        } catch (jakarta.servlet.ServletException e) {
            e.printStackTrace();
        }
        return "redirect:/home";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "auth/register";
        }
        try {
            userService.registerUser(userDto);
            return "redirect:/login?success";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "auth/register";
        }
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }
}
