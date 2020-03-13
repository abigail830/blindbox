package com.github.tuding.blindbox.api.admin.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
public class RoleDTO {

    Long id;
    String name;
    String category;
    String description;
    //Image c
    String roleImage;



}