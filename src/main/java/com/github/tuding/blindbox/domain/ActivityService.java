package com.github.tuding.blindbox.domain;

import com.github.tuding.blindbox.infrastructure.file.ImageRepository;
import com.github.tuding.blindbox.infrastructure.repository.ActivityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ActivityService {

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    ActivityRepository activityRepository;

    public void saveActivity(Activity activity) {

        final String mainImgAddr = imageRepository.saveImage(activity.getName(),
                ImageCategory.ACTIVITY, activity.getMainImg());
        activity.setMainImgAddr(mainImgAddr);

        final String contentImgAddr = imageRepository.saveImage(activity.getName(),
                ImageCategory.ACTIVITY, activity.getContentImg());
        activity.setContentImgAddr(contentImgAddr);

        activityRepository.saveActivity(activity);
    }
}
