package com.github.tuding.blindbox.api.wx;

import com.github.tuding.blindbox.api.wx.wxDto.ActivityDTO;
import com.github.tuding.blindbox.domain.ActivityService;
import com.github.tuding.blindbox.filter.NeedWxVerifyToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class WxActivityController {

    @Autowired
    ActivityService activityService;

    @GetMapping
    @NeedWxVerifyToken
    public List<ActivityDTO> getActivityForFrontPage() {
        return activityService.getAllActivitiesShownInFrontPage().stream()
                .map(ActivityDTO::new)
                .collect(Collectors.toList());
    }
}
