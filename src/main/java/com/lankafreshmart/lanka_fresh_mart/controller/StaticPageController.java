package com.lankafreshmart.lanka_fresh_mart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StaticPageController {

    @GetMapping("/privacy")
    public String privacy() {
        return "pages/privacy";
    }

    @GetMapping("/terms")
    public String terms() {
        return "pages/terms";
    }
}
