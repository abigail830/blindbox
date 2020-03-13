package com.github.tuding.blindbox.api.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
@RequestMapping("/admin-ui/roleform")
public class RoleFormController {

    @GetMapping("/")
    public String createForm(Model model) {

        return "roleform";
    }

    @PostMapping("/form")
    public void handleForm(@RequestParam("name") String name,
                             @RequestParam("description") String description,
                             @RequestParam("category") String category,
                             @RequestParam("image") MultipartFile file) {
        log.info("got request {}", name);
        log.info("file size {}", file.getSize());
    }
}
