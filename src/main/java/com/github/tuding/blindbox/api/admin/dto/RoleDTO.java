package com.github.tuding.blindbox.api.admin.dto;

import com.github.tuding.blindbox.domain.Role;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
public class RoleDTO {

    String id;
    String name;
    String category;
    String description;
    //Image c
    String roleImage;
    MultipartFile roleImageFile;

    public Role toDomainObject() {
        Role role = new Role();
        role.setId(id);
        role.setName(name);
        role.setCategory(category);
        role.setDescription(description);
        role.setRoleImage(roleImage);
        return role;
    }

    public RoleDTO(Role role) {
        this.id = role.getId();
        this.name = role.getName();
        this.category = role.getCategory();
        this.description = role.getDescription();
        this.roleImage = role.getRoleImage();
    }
}
