package com.github.tuding.blindbox.api.admin;

import com.github.tuding.blindbox.api.admin.dto.RoleDTO;
import com.github.tuding.blindbox.exception.RolesNotFoundException;
import com.github.tuding.blindbox.infrastructure.repository.RolesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/roles")
public class RolesHandler {
    @Autowired
    RolesRepository rolesRepository;

    @Value("${app.imagePath}")
    private String imagePath;

    @PostMapping("/")
    public RedirectView handleForm(@RequestParam("name") String name,
                           @RequestParam("description") String description,
                           @RequestParam("category") String category,
                           @RequestParam("image") MultipartFile file) throws IOException {
        UUID roleID = UUID.randomUUID();
        log.info("handle role creation as id {} name {} category {} description {}",
                roleID.toString(), name, category, description);
        //TODO: image format/size check
        File storeFile = new File( getRolesFolder() + roleID.toString() + ".png");
        file.transferTo(storeFile);

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(roleID.toString());
        roleDTO.setName(name);
        roleDTO.setCategory(category);
        roleDTO.setDescription(description);
        roleDTO.setRoleImage(storeFile.getCanonicalPath());
        rolesRepository.saveRole(roleDTO);
        return new RedirectView("/admin-ui/role/");

    }

    @DeleteMapping("/{name}")
    public void deleteRoles(@PathVariable("name") String name) {
        rolesRepository.deleteRoles(name);
    }

    @GetMapping("/")
    public List<RoleDTO> getRoles() {
        return rolesRepository.queryRoles();
    }

    @GetMapping("/{name}")
    public RoleDTO getRole(@PathVariable("name") String name) {
        Optional<RoleDTO> roleDTOOptional = rolesRepository.queryRolesByName(name);
        if (roleDTOOptional.isPresent()) {
            return roleDTOOptional.get();
        } else {
            throw new RolesNotFoundException();
        }
    }


    @GetMapping("/{name}/images")
    public ResponseEntity<Resource> getRoleImage(@PathVariable("name") String name) throws FileNotFoundException {
        Optional<RoleDTO> roleDTOOptional = rolesRepository.queryRolesByName(name);
        if (roleDTOOptional.isPresent()) {
            File file = new File(roleDTOOptional.get().getRoleImage());
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("image/png"))
                    .body(resource);
        } else {
            throw new RolesNotFoundException();
        }

    }

    public String getRolesFolder() {
        File rolesFolder = new File(imagePath + "/roles/");
        if (!rolesFolder.exists()) {
            rolesFolder.mkdir();
        }
        return imagePath + "/roles/";
    }

}
