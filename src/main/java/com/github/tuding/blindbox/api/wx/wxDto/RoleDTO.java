package com.github.tuding.blindbox.api.wx.wxDto;

import com.github.tuding.blindbox.domain.product.Role;
import com.github.tuding.blindbox.infrastructure.Constant;
import lombok.*;

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

    public RoleDTO(Role role) {
        this.id = role.getId();
        this.name = role.getName();
        this.category = role.getCategory();
        this.description = role.getDescription();
        this.roleImage = Constant.WX_UI_IMAGE_PATH + role.getRoleImage();
    }
}
