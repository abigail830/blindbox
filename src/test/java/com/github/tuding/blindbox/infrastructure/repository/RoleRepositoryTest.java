package com.github.tuding.blindbox.infrastructure.repository;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.github.tuding.blindbox.api.admin.dto.RoleDTO;
import com.github.tuding.blindbox.infrastructure.util.Toggle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;

@SpringBootTest
@DBRider
public class RoleRepositoryTest {
    @Autowired
    RolesRepository rolesRepository;

    @Test
    @DataSet("test-data/empty-role.yml")
    @ExpectedDataSet("expect-data/save-role.yml")
    void saveRole1() throws SQLException {
        Toggle.TEST_MODE.setStatus(true);
        rolesRepository.saveRole(new RoleDTO(1L, "testRole1", "test",
                "test", "/app/data/role/testRole1/testRole1.png"));
    }

    @Test
    @DataSet("test-data/empty-role.yml")
    void getRoleByName() throws SQLException {
        rolesRepository.queryRolesByName("testRole1");
    }
}
