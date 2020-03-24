package com.github.tuding.blindbox.api.wx;

import com.github.tuding.blindbox.api.wx.wxDto.ActivityDTO;
import com.github.tuding.blindbox.domain.ActivityService;
import com.github.tuding.blindbox.filter.NeedWxVerifyToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/wx/activities")
@Api(value = "活动相关接口", description = "活动相关接口")
public class WxActivityController {

    @Autowired
    ActivityService activityService;

    @GetMapping("/front-page")
    @NeedWxVerifyToken
    @ApiOperation(value = "获取需要显示在首页slider的活动信息(需要带token）")
    public List<ActivityDTO> getActivityForFrontPage() {
        return activityService.getAllActivitiesShownInFrontPage().stream()
                .map(ActivityDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/all")
    @NeedWxVerifyToken
    @ApiOperation(value = "获取所有活动信息，已按活动开始日期排序(需要带token）")
    public List<ActivityDTO> getAllActivity() {
        return activityService.getAllActivities().stream()
                .map(ActivityDTO::new)
                .collect(Collectors.toList());
    }

}
