package com.github.tuding.blindbox.api.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/admin-ui/productform")
public class ProductFromController {
    @GetMapping("/")
    public String createFrom(Model model,
                             @RequestParam("seriesName") String seriesName) {
        model.addAttribute("seriesName", seriesName);
        return "productform";
    }

}
