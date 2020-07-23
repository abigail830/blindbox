package com.github.tuding.blindbox.api.admin;

import com.github.tuding.blindbox.api.admin.dto.RoleDTO;
import com.github.tuding.blindbox.domain.ImageCategory;
import com.github.tuding.blindbox.domain.product.Role;
import com.github.tuding.blindbox.infrastructure.file.ImageRepository;
import com.github.tuding.blindbox.infrastructure.repository.RolesRepository;
import com.github.tuding.blindbox.infrastructure.repository.SeriesRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/admin-ui/role/v2")
public class RoleV2Controller {

    @Autowired
    RolesRepository rolesRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    SeriesRepository seriesRepository;


    @GetMapping("/")
    public String roleList(Model model) {
        List<Role> roles = rolesRepository.queryRolesOrderByName();
        List<RoleDTO> roleDTOS = roles.stream().map(RoleDTO::new).collect(Collectors.toList());
        log.info(roleDTOS.toString());
        model.addAttribute("roles", roleDTOS);
        return "role_v2";
    }

    @GetMapping("/roleform/{id}")
    public String editRole(Model model, @PathVariable String id) {

        final Optional<Role> roleOptional = rolesRepository.queryRolesByID(id);
        roleOptional.ifPresent(role -> {
                    model.addAttribute("role", new RoleDTO(role));
                    log.info("Edit roleDTO [{}]", role);
                }
        );
        return "roleform_v2";
    }

    @GetMapping("/roleform")
    public String createForm(Model model) {
        model.addAttribute("role", new RoleDTO());
        return "roleform_v2";
    }

    @PostMapping("/roleform")
    public RedirectView handleRoleForm(
            @ModelAttribute("roleForm") RoleDTO roleDTO,
            Model model) throws IOException {
        if (StringUtils.isNotBlank(roleDTO.getId())) {
            log.info("handle role update as {}, with image size {}", roleDTO, roleDTO.getRoleImageFile().getSize());

            if (roleDTO.getRoleImageFile().getSize() > 0) {
                log.info("change role image url");
                String image = imageRepository.saveImage(roleDTO.getId(), ImageCategory.ROLE, roleDTO.getRoleImageFile());
                roleDTO.setRoleImage(image + "?ts=" + System.currentTimeMillis()/1000);
            } else {
                roleDTO.setRoleImage(imageRepository.getPath(roleDTO.getId(), ImageCategory.ROLE));

            }
            rolesRepository.updateRole(roleDTO.toDomainObject());
            return new RedirectView("/admin-ui/role/v2/");
        } else {
            UUID roleID = UUID.randomUUID();
            log.info("handle role creation as {} id {}", roleDTO, roleID.toString());
            String image = imageRepository.saveImage(roleID.toString(), ImageCategory.ROLE, roleDTO.getRoleImageFile());
            roleDTO.setRoleImage(image + "?ts=" + System.currentTimeMillis()/1000);
            roleDTO.setId(roleID.toString());
            rolesRepository.saveRole(roleDTO.toDomainObject());
            return new RedirectView("/admin-ui/role/v2/");
        }
    }

    @DeleteMapping("/{id}")
    public @ResponseBody
    void deleteRoles(@PathVariable("id") String id) {
        rolesRepository.deleteRoles(id);
        final int removedMappingCount = seriesRepository.removeLinkedRolesWhenDelRole(id);
        log.info("Removed role {}. And removed {} mapping with series.", id, removedMappingCount);
    }

}
