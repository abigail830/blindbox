package com.github.tuding.blindbox.api.wx.wxDto;

import com.github.tuding.blindbox.domain.product.Draw;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DrawDTO {
    String drawId;
    String drawStatus;
    String seriesId;
    Date drawTime;
    String boxImage;
    BigDecimal price;


    public DrawDTO (Draw draw) {
        this.drawId = draw.getDrawId();
        this.drawStatus = draw.getDrawStatus();
        this.seriesId = draw.getSeriesId();
        this.drawTime = draw.getDrawTime();
        this.boxImage = draw.getBoxImage();
        this.price = draw.getPrice();
    }
}
