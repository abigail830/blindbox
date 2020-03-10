package com.github.tuding.blindbox.api;

import com.github.tuding.blindbox.api.dto.RoleDTO;
import com.github.tuding.blindbox.infrastructure.repository.RolesRepository;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.io.File;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/roles")
public class RolesHandler {
    @Autowired
    RolesRepository rolesRepository;

    @PostMapping("/{name}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求成功")})
    public void uploadRoles(@PathParam("name") String name,
                            @RequestParam("category") String category,
                            @RequestParam("description") String description,
                            @RequestParam("image")MultipartFile file) throws IOException {

        log.info("handle role creation as name {} category {} description {}", name, category, description);
        //TODO: image format/size check
        File storeFile = new File("/" + name + " .png");
        file.transferTo(storeFile);

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName(name);
        roleDTO.setCategory(category);
        roleDTO.setDescription(description);
        roleDTO.setRoleImage(storeFile.getCanonicalPath());
        rolesRepository.saveUserWithOpenId(roleDTO);
    }
}
