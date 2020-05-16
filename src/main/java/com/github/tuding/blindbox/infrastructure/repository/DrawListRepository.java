package com.github.tuding.blindbox.infrastructure.repository;

import com.github.tuding.blindbox.domain.product.Draw;
import com.github.tuding.blindbox.domain.product.DrawList;
import com.github.tuding.blindbox.domain.product.Series;
import com.github.tuding.blindbox.infrastructure.util.Toggle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class DrawListRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    DrawRepository drawRepository;

    @Autowired
    SeriesRespository seriesRespository;

    private RowMapper<DrawList> rowMapper = (resultSet, i) -> {
        DrawList drawList = new DrawList();
        drawList.setOpenId(resultSet.getString("openId"));
        drawList.setDrawListId(resultSet.getString("drawListId"));
        drawList.setSeriesId(resultSet.getString("seriesId"));
        drawList.setDrawTime(resultSet.getTimestamp("drawTime"));
        drawList.setDrawGroup(convertStringToDrawGroup(resultSet.getString("drawGroup")));
        Series series = seriesRespository.querySeriesByID(drawList.getSeriesId()).get();
        drawList.setPrice(series.getPrice());
        drawList.setBoxImage(series.getBoxImage());
        drawList.setSeriesName(series.getName());
        return drawList;
    };


    public String convertDrawGroupToString(Map<Integer, Draw> drawGroup) {
        List<String> drawIds = drawGroup.values().stream().map(Draw::getDrawId).collect(Collectors.toList());
        return String.join("#", drawIds);

    }

    public Map<Integer, Draw> convertStringToDrawGroup(String drawGroupAsString) {
        String[] split = drawGroupAsString.split("#");

        Map<Integer, Draw> drawGroup = new HashMap<>();

        for (int index = 0; index < split.length; index ++) {
            drawGroup.put(index, drawRepository.getDrawByDrawID(split[index]).get());
        }
        return drawGroup;

    }

    public void saveDrawList(DrawList drawList) {
        log.info("Going to insert draw_list_tbl for {}", drawList);

        if (Toggle.TEST_MODE.isON()) {
            String insertSql = "INSERT INTO draw_List_tbl (openId, drawListId, drawGroup, seriesId, drawTime) " +
                    "VALUES (?, ?, ?, ?, ?)";
            int update = jdbcTemplate.update(insertSql, drawList.getOpenId(), drawList.getDrawListId(),
                    convertDrawGroupToString(drawList.getDrawGroup()), drawList.getSeriesId(), drawList.getDrawTime());
            log.info("update row {} ", update);
        } else {
            String insertSql = "INSERT ignore INTO draw_List_tbl (openId, drawListId, drawGroup, seriesId, drawTime) " +
                    "VALUES (?, ?, ?, ?, ?)";
            int update = jdbcTemplate.update(insertSql, drawList.getOpenId(), drawList.getDrawListId(),
                    convertDrawGroupToString(drawList.getDrawGroup()), drawList.getSeriesId(), drawList.getDrawTime());
            log.info("update row {} ", update);
        }
    }

    public Optional<DrawList> getDrawList(String drawListId) {
        List<DrawList> drawLists =
                jdbcTemplate.query("SELECT * FROM draw_List_tbl where drawListId = ?", rowMapper, drawListId);
        return drawLists.stream().findFirst();


    }
}
