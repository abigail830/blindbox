package com.github.tuding.blindbox.api.admin;

import com.github.tuding.blindbox.api.admin.dto.RoleDTO;
import com.github.tuding.blindbox.domain.ImageCategory;
import com.github.tuding.blindbox.domain.Role;
import com.github.tuding.blindbox.exception.RolesNotFoundException;
import com.github.tuding.blindbox.infrastructure.file.ImageRepository;
import com.github.tuding.blindbox.infrastructure.repository.RolesRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/admin-ui/role")
public class RoleController {

    @Autowired
    RolesRepository rolesRepository;

    @Autowired
    ImageRepository imageRepository;


    @GetMapping("/")
    public String roleList(Model model) {
        List<Role> roles = rolesRepository.queryRoles();
        List<RoleDTO> roleDTOS = roles.stream().map(RoleDTO::new).collect(Collectors.toList());
        log.info(roleDTOS.toString());
        model.addAttribute("roles", roleDTOS);
        return "role";
    }

    @GetMapping("/roleform/{id}")
    public String editRole(Model model, @PathVariable String id) {

        final Optional<Role> roleOptional = rolesRepository.queryRolesByID(id);
        roleOptional.ifPresent(role -> {
                    model.addAttribute("role", new RoleDTO(role));
                    log.info("Edit roleDTO [{}]", role);
                }
        );
        return "roleform";
    }

    @GetMapping("/roleform")
    public String createForm(Model model) {
        model.addAttribute("role", new RoleDTO());
        return "roleform";
    }

    @PostMapping("/roleform")
    public RedirectView handleRoleForm(
            @ModelAttribute("roleForm") RoleDTO roleDTO,
            Model model) throws IOException {
        if (StringUtils.isNotBlank(roleDTO.getId())) {
            log.info("handle role update as {}, with image size {}", roleDTO, roleDTO.getRoleImageFile().getSize());

            if (roleDTO.getRoleImageFile().getSize() > 0) {
                String image = imageRepository.saveImage(roleDTO.getId(), ImageCategory.ROLE, roleDTO.getRoleImageFile());
                roleDTO.setRoleImage(image);
            }
            rolesRepository.updateRole(roleDTO.toDomainObject());
            return new RedirectView("/admin-ui/role/");
        } else {
            UUID roleID = UUID.randomUUID();
            log.info("handle role creation as {} id {}", roleDTO, roleID.toString());
            String image = imageRepository.saveImage(roleID.toString(), ImageCategory.ROLE, roleDTO.getRoleImageFile());
            roleDTO.setRoleImage(image);
            roleDTO.setId(roleID.toString());
            rolesRepository.saveRole(roleDTO.toDomainObject());
            return new RedirectView("/admin-ui/role/");
        }

    }

    @DeleteMapping("/{id}")
    public @ResponseBody
    void deleteRoles(@PathVariable("id") String id) {
        rolesRepository.deleteRoles(id);
    }

    @GetMapping("/{id}/images")
    public ResponseEntity<Resource> getRoleImage(@PathVariable("id") String id) throws FileNotFoundException {
        Optional<Role> roleOptional = rolesRepository.queryRolesByID(id);
        if (roleOptional.isPresent()) {
            File file = new File(roleOptional.get().getRoleImage());
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("image/png"))
                    .body(resource);
        } else {
            throw new RolesNotFoundException();
        }

    }

}
