package com.github.tuding.blindbox.api;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@Controller
@RequestMapping("/admin-ui/orders")
public class OrderController {

    @GetMapping("/")
    public String greeting(Model model) {
        //TODO: to query all order in brief format

        model.addAttribute("orders", new ArrayList<>());
        return "order";
    }
}
