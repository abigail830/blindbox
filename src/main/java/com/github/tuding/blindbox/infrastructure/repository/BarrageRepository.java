package com.github.tuding.blindbox.infrastructure.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Random;

@Repository
public class BarrageRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private List<String> nameList;

    @PostConstruct
    public void init() {
        nameList = jdbcTemplate.queryForList("select nick_name from barrage_tbl", String.class);
    }

    public String getRandomWxName() {
        Random random = new Random();
        return nameList.get(random.nextInt(nameList.size()));
    }

    public List<String> getLatestThreeOrderBarrage() {
        String sql = "select CONCAT(wx_user_tbl.nick_name, \"成功抽中\",order_tbl.productName)  " +
                "from order_tbl, wx_user_tbl where wx_user_tbl.open_id = order_tbl.openId  " +
                "order  by order_tbl.createTime desc limit 3";
        return jdbcTemplate.queryForList(sql, String.class);
    }
}
