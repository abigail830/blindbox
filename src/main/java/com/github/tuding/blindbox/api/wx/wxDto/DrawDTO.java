package com.github.tuding.blindbox.api.wx.wxDto;

import com.github.tuding.blindbox.domain.Draw;
import lombok.*;

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


    public DrawDTO (Draw draw) {
        this.drawId = draw.getDrawId();
        this.drawStatus = draw.getDrawStatus();
        this.seriesId = draw.getSeriesId();
        this.drawTime = draw.getDrawTime();
    }
}
