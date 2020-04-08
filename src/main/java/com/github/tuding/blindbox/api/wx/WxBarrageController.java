package com.github.tuding.blindbox.api.wx;

import com.github.tuding.blindbox.domain.BarrageService;
import com.github.tuding.blindbox.filter.NeedWxVerifyToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/wx/barrage")
@Api("弹幕相关接口")
public class WxBarrageController {

    @Autowired
    BarrageService barrageService;

    @GetMapping
    @NeedWxVerifyToken
    @ApiOperation("获取三个随机弹幕内容")
    public List<String> getThreeRandomBarrage() {
        return barrageService.getRandomBarrage();
    }
}
