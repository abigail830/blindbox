package com.github.tuding.blindbox.infrastructure.repository;

import com.github.tuding.blindbox.infrastructure.security.DefaultEncryptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Slf4j
public class AdminUserRepository {
    private static String sql = "SELECT encrypted_password from admin_user_tbl where name = ?";
    private RowMapper<String> adminUserPwdMapper = new AdminUserPwdMapper();

    public static class AdminUserPwdMapper implements RowMapper<String> {
        @Override
        public String mapRow(ResultSet resultSet, int i) throws SQLException {
            if (StringUtils.isNotBlank(resultSet.getString("encrypted_password"))) {
                return resultSet.getString("encrypted_password");
            } else {
                return null;
            }
        }
    };

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean isValidAdmin(String userName, String pwd, DefaultEncryptor defaultEncryptor) {
        try {
            List<String> encryptedPwd = jdbcTemplate.query(sql, adminUserPwdMapper, userName);
            return encryptedPwd.stream()
                    .map(item -> defaultEncryptor.decrypt(item))
                    .anyMatch(item -> pwd.equals(item));
        } catch (Exception ex) {
            log.error("Failed to verify pwd. ", ex);
            return false;
        }
    }


}
