package com.github.tuding.blindbox.domain;

import com.github.tuding.blindbox.infrastructure.file.ImageRepository;
import com.github.tuding.blindbox.infrastructure.repository.ActivityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

        activityRepository.saveActivity(activity);
    }

    public List<Activity> getAllActivities() {
        return activityRepository.queryActivities();
    }

    public List<Activity> getAllActivitiesShownInFrontPage() {
        return activityRepository.queryActivitiesShowInAd();
    }

    public Optional<Activity> getActivityById(String id) {
        return activityRepository.queryActivityById(id);
    }

    public void deleteActivityById(String id) {
        activityRepository.deleteActivity(id);
    }

    public void updateActivity(Activity activity) {

        if (!activity.getMainImg().isEmpty()) {
            final String mainImgAddr = imageRepository.saveImage(activity.getId() + "-main",
                    ImageCategory.ACTIVITY, activity.getMainImg());
            activity.setMainImgAddr(mainImgAddr);
        }

        activityRepository.updateActivity(activity);
    }
}
