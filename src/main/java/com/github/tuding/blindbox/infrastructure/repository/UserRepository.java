package com.github.tuding.blindbox.infrastructure.repository;

import com.github.tuding.blindbox.domain.User;
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
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<User> rowMapper = new BeanPropertyRowMapper<>(User.class);

    public void addUserWithOpenIdWithBonus(User user) {
        log.info("User first login: {}", user);

        String insertSql = "INSERT INTO wx_user_tbl (open_id, last_login_date, bonus) VALUES (?,?,?)";
        jdbcTemplate.update(insertSql, user.getOpenId(), user.getLastLoginDate(), user.getBonus());
    }

    public int updateLastLoginDateAndBonus(User user) {
        log.info("User login again: {}", user);

        String updateSql = "UPDATE wx_user_tbl SET last_login_date=?, bonus=? WHERE open_id=?";
        return jdbcTemplate.update(updateSql,
                user.getLastLoginDate(),
                user.getBonus(),
                user.getOpenId());
    }

    public Optional<User> getUserByOpenId(String openId) {
        List<User> users = jdbcTemplate.query("SELECT * FROM wx_user_tbl WHERE open_id = ?", rowMapper, openId);
        return users.stream().findFirst();
    }

    public List<User> getAllUser() {
        return jdbcTemplate.query("SELECT * FROM wx_user_tbl", rowMapper);
    }


    public int updateWxDetailInfo(User decryptUser) {
        log.info("Going to update wx_user_tbl for user : {}", decryptUser);

        String updateSql = "UPDATE wx_user_tbl SET gender=?, nick_name=?, city=?, country=?, " +
                "province=?, avatar_url=?, union_id=? WHERE open_id=?";

        return jdbcTemplate.update(updateSql,
                decryptUser.getGender(),
                decryptUser.getNickName(),
                decryptUser.getCity(),
                decryptUser.getCountry(),
                decryptUser.getProvince(),
                decryptUser.getAvatarUrl(),
                decryptUser.getUnionId(),
                decryptUser.getOpenId()
        );
    }
}
