package com.github.tuding.blindbox.infrastructure.repository;

import com.github.tuding.blindbox.domain.product.Role;
import com.github.tuding.blindbox.infrastructure.util.Toggle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class RolesRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Role> rowMapper = new BeanPropertyRowMapper<>(Role.class);


    public void saveRole(Role role) {
        log.info("Going to insert roles_tbl with {}", role);

        if (Toggle.TEST_MODE.isON()) {
            String insertSql = "INSERT INTO roles_tbl (id, name, role_image) " +
                    "VALUES (?, ?, ?)";
            int update = jdbcTemplate.update(insertSql, role.getId(), role.getName(), role.getRoleImage());
            log.info("update row {} ", update);
        } else {
            String insertSql = "INSERT ignore INTO roles_tbl (id, name, role_image) " +
                    "VALUES (?, ?, ?)";
            int update = jdbcTemplate.update(insertSql, role.getId(), role.getName(), role.getRoleImage());
            log.info("update row {} ", update);
        }
    }

    public Optional<Role> queryRolesByName(String name) {
        log.info("Going to query roles with name: {}", name);
        List<Role> roles = jdbcTemplate.query("SELECT * FROM roles_tbl WHERE name = ?", rowMapper, name);
        return roles.stream().findFirst();
    }

    public List<Role> queryRoles() {
        log.info("Going to query roles ");
        return jdbcTemplate.query("SELECT * FROM roles_tbl", rowMapper);
    }

    public List<Role> queryRolesOrderByName() {
        log.info("Going to query roles ");
        return jdbcTemplate.query("SELECT * FROM roles_tbl order by name", rowMapper);
    }

    public void deleteRoles(String id) {
        log.info("Delete role for {}", id);
        jdbcTemplate.update("DELETE FROM roles_tbl WHERE id = ?", id);
    }

    public Optional<Role> queryRolesByID(String id) {
        log.info("Going to query roles with id: {}", id);
        List<Role> roles = jdbcTemplate.query("SELECT * FROM roles_tbl WHERE id = ?", rowMapper, id);
        return roles.stream().findFirst();
    }

    public void updateRole(Role role) {
        String insertSql = "UPDATE roles_tbl SET name = ? WHERE id = ? ";
        int update = jdbcTemplate.update(insertSql, role.getName(), role.getId());
        log.info("update row {} ", update);
    }
}
