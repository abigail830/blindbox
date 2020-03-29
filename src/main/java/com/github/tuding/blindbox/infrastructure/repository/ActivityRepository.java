package com.github.tuding.blindbox.infrastructure.repository;


import com.github.tuding.blindbox.domain.Activity;
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
public class ActivityRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private RowMapper<Activity> rowMapper = new BeanPropertyRowMapper<>(Activity.class);

    public void saveActivity(Activity activity) {
        log.info("Going to insert activity_tbl with name: {}", activity);

        if (Toggle.TEST_MODE.isON()) {
            String insertSql = "INSERT INTO activity_tbl (id, activity_name, activity_description, shown_in_ad, " +
                    "main_img_addr, activity_start_date, activity_end_date) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            int update = jdbcTemplate.update(insertSql,
                    activity.getId(),
                    activity.getActivityName(),
                    activity.getActivityDescription(),
                    activity.getShownInAd(),
                    activity.getMainImgAddr(),
                    activity.getActivityStartDate(),
                    activity.getActivityEndDate()
            );
            log.info("update row {} ", update);
        } else {
            String insertSql = "INSERT ignore INTO activity_tbl (id, activity_name, activity_description, shown_in_ad, " +
                    "main_img_addr, activity_start_date, activity_end_date) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            int update = jdbcTemplate.update(insertSql,
                    activity.getId(),
                    activity.getActivityName(),
                    activity.getActivityDescription(),
                    activity.getShownInAd(),
                    activity.getMainImgAddr(),
                    activity.getActivityStartDate(),
                    activity.getActivityEndDate()
            );
            log.info("update row {} ", update);
        }
    }

    public List<Activity> queryActivities() {
        log.info("Going to query activitys ");
        return jdbcTemplate.query("SELECT * FROM activity_tbl ORDER BY activity_start_date DESC", rowMapper);
    }

    public void deleteActivity(String id) {
        log.info("Delete activity for {}", id);
        jdbcTemplate.update("DELETE FROM activity_tbl WHERE id = ?", id);
    }

    public Optional<Activity> queryActivityById(String id) {
        log.info("Going to query activity with id: {}", id);
        List<Activity> activities = jdbcTemplate.query("SELECT * FROM activity_tbl WHERE id = ?", rowMapper, id);
        return activities.stream().findFirst();
    }

    public void updateActivity(Activity activity) {
        log.info("Going to update activity_tbl for activity : {}", activity);

        String updateSQL = "UPDATE activity_tbl " +
                " SET activity_name=?, activity_description=?, shown_in_ad=?" +
                ", activity_start_date=?, activity_end_date=?, main_img_addr=? " +
                " WHERE id = ?";
        int update = jdbcTemplate.update(updateSQL,
                activity.getActivityName(),
                activity.getActivityDescription(),
                activity.getShownInAd(),
                activity.getActivityStartDate(),
                activity.getActivityEndDate(),
                activity.getMainImgAddr(),
                activity.getId());
        log.info("update row {} ", update);
    }

    public List<Activity> queryActivitiesShowInAd() {
        log.info("Going to query activitys ");
        return jdbcTemplate.query("SELECT * FROM activity_tbl WHERE shown_in_ad = true ORDER BY activity_start_date DESC", rowMapper);

    }

    public void addNotification(Activity activity) {
        log.info("Going to update notify for activity : {}", activity);
        String updateSQL = "UPDATE activity_tbl SET notify=?, redirect_page=? WHERE id = ?";
        int update = jdbcTemplate.update(updateSQL,
                activity.getNotify(), activity.getNotifyJumpPage(), activity.getId());

        log.info("update activity_tbl row {} ", update);
    }
}
