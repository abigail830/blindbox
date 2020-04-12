package com.github.tuding.blindbox.api.wx;

import com.github.tuding.blindbox.api.wx.wxDto.*;
import com.github.tuding.blindbox.domain.product.DrawService;
import com.github.tuding.blindbox.domain.product.Product;
import com.github.tuding.blindbox.domain.product.ProductService;
import com.github.tuding.blindbox.domain.user.UserService;
import com.github.tuding.blindbox.filter.NeedWxVerifyToken;
import com.github.tuding.blindbox.infrastructure.Constant;
import com.github.tuding.blindbox.infrastructure.security.Jwt;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/wx/products")
@Slf4j
@Api("产品相关接口")
public class WxProductController {

    @Autowired
    Jwt jwt;

    @Autowired
    private ProductService productService;

    @Autowired
    private DrawService drawService;

    @Autowired
    private UserService userService;

    @GetMapping("/roles")
    @NeedWxVerifyToken
    @ApiOperation("获取所有产品角色列表(需要带token)")
    public List<RoleDTO> getRoles() {
        return productService.getRoles().stream().map(RoleDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/{roleId}/series")
    @NeedWxVerifyToken
    @ApiOperation("根据角色ID，获取关联产品系列(需要带token)")
    public List<SeriesDTO> getSeriesListByRole(@PathVariable("roleId") String roleId) {
        return productService.getSeriesList(roleId).stream().map(SeriesDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/series/{seriesId}")
    @NeedWxVerifyToken
    @ApiOperation("根据ID，获取指定产品系列， 包括产品数量(需要带token)")
    public SeriesDTO getSeries(@PathVariable("seriesId") String seriesId) {
        return new SeriesDTO(productService.getSeries(seriesId).get(), productService.getProduct(seriesId));
    }
    @GetMapping("/series/{seriesId}/products")
    @NeedWxVerifyToken
    @ApiOperation("根据产品系列ID，获取产品列表(需要带token)")
    public List<ProductDTO> getProductBySeriesId(@PathVariable("seriesId") String seriesId) {
        return productService.getProductWithPrice(seriesId).stream().map(ProductDTO::new).collect(Collectors.toList());
    }


    @GetMapping("/series/new")
    @NeedWxVerifyToken
    @ApiOperation("获取所有新品系列(需要带token)")
    public List<SeriesDTO> getAllNewSeries() {
        return productService.getAllNewSeries().stream()
                .map(SeriesDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/series/all/paging")
    @NeedWxVerifyToken
    @ApiOperation("分页获取所有产品系列(需要带token), numOfPage start from 0")
    public List<SeriesDTO> getAllNewSeries(@RequestParam Integer limitPerPage, Integer numOfPage) {
        return productService.getAllSeries(limitPerPage, numOfPage).stream()
                .map(SeriesDTO::new).collect(Collectors.toList());
    }


    @PutMapping("/draw/{seriesId}")
    @NeedWxVerifyToken
    @ApiOperation("在指定产品系列下抽一盒， 返回抽盒信息(需要带token)")
    public DrawDTO drawAProduct(HttpServletRequest request,  @PathVariable("seriesId") String seriesId) {
        String token = request.getHeader(Constant.HEADER_AUTHORIZATION);
        return new DrawDTO(drawService.drawAProduct(jwt.getOpenIdFromToken(token),seriesId));
    }

    @GetMapping("/draw/")
    @NeedWxVerifyToken
    @ApiOperation("获取当前的抽盒 (需要带token) - 此接口已停用")
    public DrawDTO getADrawForUserOpenID(HttpServletRequest request) {
        log.warn("getADrawForUserOpenID 接口应停用");
        String token = request.getHeader(Constant.HEADER_AUTHORIZATION);
        return new DrawDTO(drawService.getDrawByOpenID(jwt.getOpenIdFromToken(token)));
    }

    @GetMapping("/draw/{drawId}")
    @NeedWxVerifyToken
    @ApiOperation("获取当前的抽盒 (需要带token)")
    public DrawDTO getADrawForDrawId(@PathVariable String drawId) {
        return new DrawDTO(drawService.getDrawByDrawID(drawId));
    }

    @GetMapping("/draw/{drawId}/product")
    @NeedWxVerifyToken
    @ApiOperation("获取当前的抽盒 (需要带token)")
    public ProductDTO getProductByDrawId(@PathVariable String drawId) {
        return new ProductDTO(productService.getProductWithoutPrice(drawId));
    }

    @DeleteMapping("/draw/")
    @NeedWxVerifyToken
    @ApiOperation("取消已有的抽盒 (需要带token) - 此接口已停用")
    public void cancelADrawForUserOpenID(HttpServletRequest request) {
        log.warn("cancelADrawForUserOpenID 接口应停用");
        String token = request.getHeader(Constant.HEADER_AUTHORIZATION);
        drawService.cancelADrawByOpenID(jwt.getOpenIdFromToken(token));
    }

    @DeleteMapping("/draw/{drawId}")
    @NeedWxVerifyToken
    @ApiOperation("取消已有的抽盒 (需要带token)")
    public void cancelADrawForDrawID(@PathVariable String drawId) {
        drawService.cancelADrawByDrawId(drawId);
    }

    @PostMapping("/use-discount/{drawId}")
    @NeedWxVerifyToken
    @ApiOperation("扣减积分以兑换优惠券, 返回折后价格/折扣描述/剩余积分 (需要带token)")
    @Transactional
    public DiscountCouponDTO getDiscountByBonus(HttpServletRequest request,
                                                @PathVariable String drawId) {
        BigDecimal priceAfterDiscount = productService.getProductPriceAfterDiscount(drawId);

        String token = request.getHeader(Constant.HEADER_AUTHORIZATION);
        Integer remainBonus = userService.consumeBonusForCoupon(token, Constant.GET_COUPON_CONSUME_BONUS);

        drawService.updateDrawPriceById(priceAfterDiscount, drawId);

        return new DiscountCouponDTO(priceAfterDiscount, remainBonus);
    }

    @PostMapping("/use-tips/{drawId}")
    @NeedWxVerifyToken
    @ApiOperation(value = "扣减积分以兑换提示券, 返回提示信息/剩余积分 (需要带token)")
    public TipsCouponDTO getTipsByBonus(HttpServletRequest request,
                                                @PathVariable String drawId) {
        String token = request.getHeader(Constant.HEADER_AUTHORIZATION);
        Integer remainBonus = userService.consumeBonusForCoupon(token, Constant.GET_COUPON_CONSUME_BONUS);
        Product excludedProduct = drawService.getExcludedProduct(drawId);
        return new TipsCouponDTO(excludedProduct, remainBonus);
    }

    @PostMapping("/use-display/{drawId}")
    @NeedWxVerifyToken
    @ApiOperation(value = "扣减积分以兑换提示券, 返回抽中产品信息/剩余积分 (需要带token)")
    public DisplayCouponDTO getDisplayByBonus(HttpServletRequest request,
                                        @PathVariable String drawId) {
        String token = request.getHeader(Constant.HEADER_AUTHORIZATION);
        Integer remainBonus = userService.consumeBonusForCoupon(token, Constant.GET_COUPON_CONSUME_BONUS);
        Product excludedProduct = drawService.getDrawProduct(drawId);
        return new DisplayCouponDTO(excludedProduct, remainBonus);
    }

}
