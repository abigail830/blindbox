package com.github.tuding.blindbox.api;

import com.github.tuding.blindbox.api.admin.dto.RoleDTO;
import com.github.tuding.blindbox.infrastructure.repository.RolesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/admin-ui/series")
public class SeriesController {

    @Autowired
    RolesRepository rolesRepository;

    @GetMapping("/")
    public String createForm(Model model,
                             @RequestParam("rolename") String name) {
        Optional<RoleDTO> roleDTOOptional = rolesRepository.queryRolesByName(name);
        model.addAttribute("role", roleDTOOptional.get());
        return "series";
    }
}
