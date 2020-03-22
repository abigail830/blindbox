package com.github.tuding.blindbox.api.wx;

import com.github.tuding.blindbox.api.wx.wxDto.DrawDTO;
import com.github.tuding.blindbox.api.wx.wxDto.RoleDTO;
import com.github.tuding.blindbox.api.wx.wxDto.SeriesDTO;
import com.github.tuding.blindbox.domain.ProductService;
import com.github.tuding.blindbox.filter.NeedWxVerifyToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/wx/products")
@Slf4j
@Api(value = "产品相关接口", description = "产品相关接口（角色/系列/产品）")
public class WxProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/roles")
    @NeedWxVerifyToken
    @ApiOperation(value = "获取所有产品角色列表(需要带token)")
    public List<RoleDTO> getRoles() {
        return productService.getRoles().stream().map(RoleDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/{roleId}/series")
    @NeedWxVerifyToken
    @ApiOperation(value = "根据角色ID，获取关联产品系列(需要带token)")
    public List<SeriesDTO> getSeriesListByRole(@PathVariable("roleId") String roleId) {
        return productService.getSeriesList(roleId).stream().map(SeriesDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/series/{seriesId}")
    @NeedWxVerifyToken
    @ApiOperation(value = "根据ID，获取指定产品系列(需要带token)")
    public SeriesDTO getSeries(@PathVariable("seriesId") String seriesId) {
        return new SeriesDTO(productService.getSeries(seriesId).get());
    }


    @PutMapping("/draw/{seriesId}")
    @NeedWxVerifyToken
    @ApiOperation(value = "under development")
    public DrawDTO drawAProduct(@PathVariable("seriesId") String seriesId) {
        return productService.putADraw(seriesId);
    }

    @PutMapping("/order/{drawId}")
    @NeedWxVerifyToken
    @ApiOperation(value = "under development")
    public DrawDTO confirmADraw(@PathVariable("drawId") String drawId) {
        return productService.confirmADraw(drawId);
    }
}
