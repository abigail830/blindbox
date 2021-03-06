package com.github.tuding.blindbox.api.admin.dto;

import com.github.tuding.blindbox.domain.product.Role;
import com.github.tuding.blindbox.infrastructure.Constant;
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
    //Image c
    String roleImage;
    MultipartFile roleImageFile;

    public Role toDomainObject() {
        Role role = new Role();
        role.setId(id);
        role.setName(name);
        role.setRoleImage(roleImage);
        return role;
    }

    public RoleDTO(Role role) {
        this.id = role.getId();
        this.name = role.getName();
        this.roleImage = Constant.ADMIN_UI_IMAGE_PATH + role.getRoleImage();
    }
}
