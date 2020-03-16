package com.github.tuding.blindbox.domain;

import com.github.tuding.blindbox.infrastructure.file.ImageRepository;
import com.github.tuding.blindbox.infrastructure.repository.ActivityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ActivityService {

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    ActivityRepository activityRepository;

    public void saveActivity(Activity activity) {
        UUID uuid = UUID.randomUUID();
        activity.setId(uuid.toString());

        if (!activity.getMainImg().isEmpty()) {
            final String mainImgAddr = imageRepository.saveImage(uuid.toString() + "-main",
                    ImageCategory.ACTIVITY, activity.getMainImg());
            activity.setMainImgAddr(mainImgAddr);
        }

        if (!activity.getContentImg().isEmpty()) {
            final String contentImgAddr = imageRepository.saveImage(uuid.toString() + "-content",
                    ImageCategory.ACTIVITY, activity.getContentImg());
            activity.setContentImgAddr(contentImgAddr);
        }

        activityRepository.saveActivity(activity);
    }

    public List<Activity> getAllActivities() {
        return activityRepository.queryActivities();
    }
}
