package com.github.tuding.blindbox.api;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@Controller
@RequestMapping("/admin-ui/activities")
public class ActivityController {

    @GetMapping("/")
    public String homepage(Model model) {
        //TODO: to query all activities in brief format

        model.addAttribute("activities", new ArrayList<>());
        return "activity";
    }
}
