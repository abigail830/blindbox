package com.github.tuding.blindbox.domain.user;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TransportFee {

    String area;
    BigDecimal transportFee;
    String remarks;
}
