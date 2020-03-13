package com.github.tuding.blindbox.infrastructure.repository;

import com.github.tuding.blindbox.api.admin.dto.RoleDTO;
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

    private RowMapper<RoleDTO> rowMapper = new BeanPropertyRowMapper<>(RoleDTO.class);


    public void saveUserWithOpenId(RoleDTO roleDTO) {
        log.info("Going to insert roles_tbl with name: {}", roleDTO.getName());

        if (Toggle.TEST_MODE.isON()) {
            String insertSql = "INSERT INTO roles_tbl (name, category, description, role_image) " +
                    "VALUES (?, ?, ?, ?)";
            int update = jdbcTemplate.update(insertSql, roleDTO.getName(), roleDTO.getCategory(), roleDTO.getDescription(), roleDTO.getRoleImage());
            log.info("update row {} ", update);
        } else {
            String insertSql = "INSERT ignore INTO roles_tbl (name, category, description, role_image) " +
                    "VALUES (?, ?, ?, ?)";
            int update = jdbcTemplate.update(insertSql, roleDTO.getName(), roleDTO.getCategory(), roleDTO.getDescription(), roleDTO.getRoleImage());
            log.info("update row {} ", update);
        }
    }

    public Optional<RoleDTO> queryRolesByName(String name) {
        log.info("Going to query roles with name: {}", name);
        List<RoleDTO> roleDTOs = jdbcTemplate.query("SELECT * FROM roles_tbl WHERE name = ?", rowMapper, name);
        return roleDTOs.stream().findFirst();
    }

    public List<RoleDTO> queryRoles() {
        log.info("Going to query roles ");
        return jdbcTemplate.query("SELECT * FROM roles_tbl", rowMapper);
    }

    public void deleteRoles(String name) {
        log.info("Delete role for {}", name);
        jdbcTemplate.update("DELETE FROM roles_tbl WHERE name = ?", name);
    }
}
