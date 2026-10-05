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
    private final com.lankafreshmart.lanka_fresh_mart.service.ProductService productService;
    private final org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

    @GetMapping("/login")
    public String login(Model model) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new UserDto());
        }
        return "auth/login";
    }



    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new UserDto());
        }
        return "auth/login";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "auth/login";
        }
        try {
            userService.registerUser(userDto);
            return "redirect:/login?success";
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            model.addAttribute("error", "This email address is already registered. Please sign in instead.");
            return "auth/login";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "auth/login";
        }
    }

    @PostMapping("/request-reset-otp")
    public String requestResetOtp(@org.springframework.web.bind.annotation.RequestParam String resetEmail, Model model) {
        try {
            userService.requestPasswordReset(resetEmail);
            return "redirect:/login?otpSent=" + resetEmail;
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/login?resetError=" + e.getMessage();
        }
    }

    @PostMapping("/reset-password")
    public String resetPassword(@org.springframework.web.bind.annotation.RequestParam String resetEmail, 
                                @org.springframework.web.bind.annotation.RequestParam String otpCode,
                                @org.springframework.web.bind.annotation.RequestParam String newPassword, 
                                Model model) {
        try {
            userService.resetPasswordWithOtp(resetEmail, otpCode, newPassword);
            return "redirect:/login?resetSuccess";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/login?otpSent=" + resetEmail + "&resetError=" + e.getMessage();
        }
    }

    @GetMapping("/home")
    public String home(org.springframework.ui.Model model) {
        model.addAttribute("mostSellingProduct", productService.getMostSellingProduct());
        model.addAttribute("mostSellingProducts", productService.getMostSellingProducts(5));
        model.addAttribute("weeklyBestSellingMap", productService.getWeeklyBestSellingByCategoryMap(5));
        return "home";
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }
}
