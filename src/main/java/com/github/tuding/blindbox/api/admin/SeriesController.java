package com.github.tuding.blindbox.api.admin;

import com.github.tuding.blindbox.api.admin.dto.SeriesDTO;
import com.github.tuding.blindbox.infrastructure.repository.RolesRepository;
import com.github.tuding.blindbox.infrastructure.repository.SeriesRespository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin-ui/series")
public class SeriesController {

    @Autowired
    SeriesRespository seriesRespository;

    @Autowired
    RolesRepository rolesRepository;

    @GetMapping("/")
    public String seriesPage(Model model,
                           @RequestParam("roleID") Long roleID) {

        List<SeriesDTO> seriesDTOs = seriesRespository.queryByRoleID(roleID);
        model.addAttribute("series", seriesDTOs);
        model.addAttribute("role", rolesRepository.queryRolesByName(roleID).get());
        return "series";
    }
}
