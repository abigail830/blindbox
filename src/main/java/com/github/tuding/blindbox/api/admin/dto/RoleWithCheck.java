package com.github.tuding.blindbox.api.admin.dto;

import com.github.tuding.blindbox.domain.product.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class RoleWithCheck extends Role {
    Boolean checked;

    public RoleWithCheck(Role role) {
        super(role.getId(), role.getName(), role.getRoleImage());
        this.checked = Boolean.FALSE;
    }

    public RoleWithCheck(Role role, Boolean checked) {
        this(role);
        this.checked = checked;
    }
}
