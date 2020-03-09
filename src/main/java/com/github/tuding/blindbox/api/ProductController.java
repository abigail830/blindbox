package com.github.tuding.blindbox.api;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@Controller
@RequestMapping("/admin-ui/products")
public class ProductController {

    @GetMapping("/")
    public String greeting(Model model) {
        model.addAttribute("products", new ArrayList<>());
        return "product";
    }
}
