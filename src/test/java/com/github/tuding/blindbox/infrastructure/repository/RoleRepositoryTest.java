package com.github.tuding.blindbox.infrastructure.repository;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.github.tuding.blindbox.api.admin.dto.RoleDTO;
import com.github.tuding.blindbox.domain.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.Assert.assertTrue;

@SpringBootTest
@DBRider
public class RoleRepositoryTest {
    @Autowired
    RolesRepository rolesRepository;


    @Test
    @DataSet("expect-data/save-product.yml")
    void getRoleByName()  {
        Optional<Role> role = rolesRepository.queryRolesByName("testRole1");
        assertTrue(role.isPresent());


    }
}
