package com.github.tuding.blindbox.domain.activity;

import com.github.tuding.blindbox.domain.ImageCategory;
import com.github.tuding.blindbox.exception.ActivityNotFoundException;
import com.github.tuding.blindbox.exception.BizException;
import com.github.tuding.blindbox.exception.ErrorCode;
import com.github.tuding.blindbox.infrastructure.client.WxClient;
import com.github.tuding.blindbox.infrastructure.file.ImageRepository;
import com.github.tuding.blindbox.infrastructure.repository.ActivityRepository;
import com.github.tuding.blindbox.infrastructure.repository.UserRepository;
import com.github.tuding.blindbox.infrastructure.security.Jwt;
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

    @Autowired
    UserRepository userRepository;

    @Autowired
    WxClient wxClient;

    @Autowired
    Jwt jwt;


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

        if (!activity.getContentImg().isEmpty()) {
            final String contentImgAddr = imageRepository.saveImage(activity.getId() + "-content",
                    ImageCategory.ACTIVITY, activity.getContentImg());
            activity.setContentImgAddr(contentImgAddr);
        }

        activityRepository.updateActivity(activity);
    }

    public void acceptActivityNotify(String token, String activityId, String redirectPage) {
        String openId = jwt.getOpenIdFromToken(token);
        log.info("User[{}] register notify for activity[{}]", openId, activityId);

        final Optional<Activity> activity = activityRepository.queryActivityById(activityId);
        if (!activity.isPresent()) {
            throw new BizException(ErrorCode.INVALID_ACTIVITY_ID);
        }

        final Activity existingRecord = activity.get();
        //add bonus
        if (!existingRecord.isExistingSubscriber(openId)) {
            userRepository.addBonus(openId, existingRecord.getGiftBonus());
            log.info("Added bonus for user {} when 1st subscribe to activity {}", openId, activityId);
        }
        //add notifier
        existingRecord.addNotifyInfo(openId, redirectPage);
        activityRepository.addNotification(existingRecord);
    }

    public void sendActivityNotify(String activityId) {
        final Activity activity = activityRepository.queryActivityById(activityId)
                .orElseThrow(() -> new BizException(ErrorCode.INVALID_ACTIVITY_ID));
        wxClient.sendActivityNotify(activity);
    }

    public Activity getActivityDetail(String activityId) {
        return activityRepository.queryActivityById(activityId)
                .orElseThrow(ActivityNotFoundException::new);
    }
}
