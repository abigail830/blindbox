package com.github.tuding.blindbox.infrastructure.repository;

import com.github.tuding.blindbox.api.dto.RoleDTO;
import com.github.tuding.blindbox.infrastructure.util.Toggle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class RolesRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void saveUserWithOpenId(RoleDTO roleDTO) {
        log.info("Going to insert role_tbl with name: {}", roleDTO.getName());

        if (Toggle.TEST_MODE.isON()) {
            String insertSql = "INSERT INTO role_tbl (name, category, description, role_image) " +
                    "VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(insertSql, roleDTO.getName(), roleDTO.getCategory(), roleDTO.getDescription(), roleDTO.getRoleImage());
        } else {
            String insertSql = "INSERT ignore INTO role_tbl (name, category, description, role_image) " +
                    "VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(insertSql, roleDTO.getName(), roleDTO.getCategory(), roleDTO.getDescription(), roleDTO.getRoleImage());
        }
    }
}
