package com.github.tuding.blindbox.api.wx;

import com.github.tuding.blindbox.api.wx.wxDto.DrawDTO;
import com.github.tuding.blindbox.api.wx.wxDto.RoleDTO;
import com.github.tuding.blindbox.api.wx.wxDto.SeriesDTO;
import com.github.tuding.blindbox.domain.ProductService;
import com.github.tuding.blindbox.filter.NeedWxVerifyToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/wx/products")
@Slf4j
public class WxProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/roles")
    @NeedWxVerifyToken
    public List<RoleDTO> getRoles() {
        return productService.getRoles().stream().map(RoleDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/{roleId}/series")
    @NeedWxVerifyToken
    public List<SeriesDTO> getSeriesListByRole(@PathVariable("roleId") String roleId) {
        return productService.getSeriesList(roleId).stream().map(SeriesDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/series/{seriesId}")
    @NeedWxVerifyToken
    public SeriesDTO getSeries(@PathVariable("seriesId") String roleId) {
        return new SeriesDTO(productService.getSeries(roleId).get());
    }


    @PutMapping("/draw/{seriesId}")
    @NeedWxVerifyToken
    public DrawDTO drawAProduct(@PathVariable("seriesId") String seriesId) {
        return productService.putADraw(seriesId);
    }

    @PutMapping("/order/{drawId}")
    @NeedWxVerifyToken
    public DrawDTO confirmADraw(@PathVariable("drawId") String drawId) {
        return productService.confirmADraw(drawId);
    }
}
