package com.github.tuding.blindbox.api.wx;

import com.github.tuding.blindbox.api.wx.wxDto.DrawDTO;
import com.github.tuding.blindbox.api.wx.wxDto.RoleDTO;
import com.github.tuding.blindbox.api.wx.wxDto.SeriesDTO;
import com.github.tuding.blindbox.domain.DrawService;
import com.github.tuding.blindbox.domain.ProductService;
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
@RequestMapping("/wx/products")
@Slf4j
@Api(value = "产品相关接口", description = "产品相关接口（角色/系列/产品）")
public class WxProductController {

    @Autowired
    Jwt jwt;

    @Autowired
    private ProductService productService;

    @Autowired
    private DrawService drawService;

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

    @GetMapping("/series/new")
    @NeedWxVerifyToken
    @ApiOperation(value = "获取所有新品系列(需要带token)")
    public List<SeriesDTO> getAllNewSeries() {
        return productService.getAllNewSeries().stream()
                .map(SeriesDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/series/all/paging")
    @NeedWxVerifyToken
    @ApiOperation(value = "分页获取所有产品系列(需要带token), numOfPage start from 0")
    public List<SeriesDTO> getAllNewSeries(@RequestParam Integer limitPerPage, Integer numOfPage) {
        return productService.getAllSeries(limitPerPage, numOfPage).stream()
                .map(SeriesDTO::new).collect(Collectors.toList());
    }


    @PutMapping("/draw/{seriesId}")
    @NeedWxVerifyToken
    @ApiOperation(value = "under development")
    public DrawDTO drawAProduct(HttpServletRequest request,  @PathVariable("seriesId") String seriesId) {
        String token = request.getHeader(Constant.HEADER_AUTHORIZATION);
        return new DrawDTO(drawService.drawAProduct(jwt.getOpenIdFromToken(token),seriesId));
    }

    @PutMapping("/order/{drawId}")
    @NeedWxVerifyToken
    @ApiOperation(value = "under development")
    public DrawDTO confirmADraw(@PathVariable("drawId") String drawId) {
        return new DrawDTO(drawService.confirmADraw(drawId));
    }
}
