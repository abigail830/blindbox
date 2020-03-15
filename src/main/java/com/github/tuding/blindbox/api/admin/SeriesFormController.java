package com.github.tuding.blindbox.api.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/admin-ui/seriesform")
public class SeriesFormController {
    @GetMapping("/")
    public String createForm(Model model,
                             @RequestParam("roleName") String roleName) {
        model.addAttribute("roleName", roleName);
        return "seriesform";
    }


}
