package com.github.tuding.blindbox.domain.order;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class PreOrder {

    String prepayId;
    String nonceStr;
    String preOrderTime;
    String paySign;

}
