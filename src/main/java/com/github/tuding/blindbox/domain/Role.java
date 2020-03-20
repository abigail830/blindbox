package com.github.tuding.blindbox.domain;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
public class Role {
    String id;
    String name;
    String category;
    String description;
    //Image c
    String roleImage;
}
