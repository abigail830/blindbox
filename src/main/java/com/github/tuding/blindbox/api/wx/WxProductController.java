package com.github.tuding.blindbox.api.wx;

import com.github.tuding.blindbox.api.wx.wxDto.DrawDTO;
import com.github.tuding.blindbox.domain.ProductService;
import com.github.tuding.blindbox.domain.Role;
import com.github.tuding.blindbox.domain.Series;
import com.github.tuding.blindbox.filter.NeedWxVerifyToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wx/products")
@Slf4j
public class WxProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/roles")
    @NeedWxVerifyToken
    public List<Role> getRoles() {
        return productService.getRoles();
    }

    @GetMapping("/{roleId}/series")
    @NeedWxVerifyToken
    public List<Series> getSeriesListByRole(@PathVariable("roleId") String roleId) {
        return productService.getSeriesList(roleId);
    }

    @GetMapping("/series/{seriesId}")
    @NeedWxVerifyToken
    public Series getSeries(@PathVariable("seriesId") String roleId) {
        return productService.getSeries(roleId).get();
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
