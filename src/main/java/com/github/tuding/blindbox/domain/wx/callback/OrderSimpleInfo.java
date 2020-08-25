package com.github.tuding.blindbox.domain.wx.callback;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderSimpleInfo {

    private String productId;
    private Timestamp orderCreateTime;
    private String seriesId;
}
