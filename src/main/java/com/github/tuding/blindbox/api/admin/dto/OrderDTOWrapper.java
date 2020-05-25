package com.github.tuding.blindbox.api.admin.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
public class OrderDTOWrapper {
    List<OrderDTO> data;
}
