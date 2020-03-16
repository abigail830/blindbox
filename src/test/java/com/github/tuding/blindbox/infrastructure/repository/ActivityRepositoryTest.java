package com.github.tuding.blindbox.infrastructure.repository;

import com.github.database.rider.spring.api.DBRider;
import com.github.tuding.blindbox.domain.Activity;
import com.github.tuding.blindbox.infrastructure.util.Toggle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@DBRider
class ActivityRepositoryTest {

    @Autowired
    ActivityRepository activityRepository;

    @Test
    void saveActivity() {
        Toggle.TEST_MODE.setStatus(true);

        final Activity activity = new Activity("id", "name", "description",
                Boolean.TRUE, new Date(), new Date(), "mainimage", "contentimage");
        //when
        activityRepository.saveActivity(activity);

        final List<Activity> act = activityRepository.queryActivities().stream()
                .filter(activity1 -> activity1.getId().equals("id")).collect(Collectors.toList());

        System.out.println(act);
        assertEquals(1, act.size());
        assertFalse(act.get(0).getLastUpdateTime().toString().isEmpty());

        activityRepository.deleteActivity("id");

        assertFalse(activityRepository.queryActivityById("id").isPresent());

    }
}