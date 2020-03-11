package com.github.tuding.blindbox.api;

import com.github.tuding.blindbox.api.dto.RoleDTO;
import com.github.tuding.blindbox.infrastructure.repository.RolesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin-ui/products")
public class ProductController {

    @Autowired
    RolesRepository rolesRepository;

    @GetMapping("/")
    public String greeting(Model model) {
        List<RoleDTO> roleDTOS = rolesRepository.queryRoles();
        log.info(roleDTOS.toString());
        model.addAttribute("roles", roleDTOS);
        model.addAttribute("products", new ArrayList<>());
        return "product";
    }
}
