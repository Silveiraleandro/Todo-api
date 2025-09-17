/*
 * © 2025 Leandro Silveira. All rights reserved.
 */
package com.example.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String root() {
        return "redirect:/swagger-ui.html";
        // return "redirect:/swagger-ui/index.html
    }
}
