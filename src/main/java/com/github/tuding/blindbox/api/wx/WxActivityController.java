package com.github.tuding.blindbox.api.wx;

import com.github.tuding.blindbox.api.wx.wxDto.ActivityBriefDTO;
import com.github.tuding.blindbox.api.wx.wxDto.ActivityDetailDTO;
import com.github.tuding.blindbox.api.wx.wxDto.ActivityRegisterFlag;
import com.github.tuding.blindbox.domain.activity.ActivityService;
import com.github.tuding.blindbox.filter.NeedWxVerifyToken;
import com.github.tuding.blindbox.infrastructure.Constant;
import com.github.tuding.blindbox.infrastructure.security.Jwt;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/wx/activities")
@Api("活动相关接口")
public class WxActivityController {

    @Autowired
    ActivityService activityService;

    @Autowired
    Jwt jwt;

    @GetMapping("/front-page")
    @NeedWxVerifyToken
    @ApiOperation(value = "获取需要显示在首页slider的活动信息概要(需要带token）")
    public List<ActivityBriefDTO> getActivityForFrontPage() {
        return activityService.getAllActivitiesShownInFrontPage().stream()
                .map(ActivityBriefDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/all")
    @NeedWxVerifyToken
    @ApiOperation(value = "获取活动概要列表，已按活动开始日期排序(需要带token）")
    public List<ActivityBriefDTO> getAllActivity() {
        return activityService.getAllActivities().stream()
                .map(ActivityBriefDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/id/{activityId}")
    @NeedWxVerifyToken
    @ApiOperation(value = "根据活动ID，获取活动详情(需要带token")
    public ActivityDetailDTO getActivityDetail(@PathVariable String activityId) {
        return new ActivityDetailDTO(activityService.getActivityDetail(activityId));
    }

    @PutMapping("/id/{activityId}/accept-notify")
    @NeedWxVerifyToken
    @ApiOperation(value = "接受活动开始通知(需要带token")
    public void registerForActivityNotify(HttpServletRequest request,
                                          @PathVariable String activityId,
                                          @RequestParam String redirectPage) {
        String token = request.getHeader(Constant.HEADER_AUTHORIZATION);
        activityService.acceptActivityNotify(token, activityId, redirectPage);
    }

    @GetMapping("/id/{activityId}/accept-notify")
    @NeedWxVerifyToken
    @ApiOperation(value = "判断用户是否已经订阅了活动开始通知(需要带token")
    public ActivityRegisterFlag ifUserRegisterActivity(HttpServletRequest request,
                                                       @PathVariable String activityId) {
        String token = request.getHeader(Constant.HEADER_AUTHORIZATION);
        final String openId = jwt.getOpenIdFromToken(token);

        final Boolean reg = activityService.ifRegisterActivity(activityId, openId);
        return new ActivityRegisterFlag(reg);
    }

}
