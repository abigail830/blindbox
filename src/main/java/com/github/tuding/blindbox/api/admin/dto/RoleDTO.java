package com.github.tuding.blindbox.api.admin.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
public class RoleDTO {

    String id;
    String name;
    String category;
    String description;
    //Image c
    String roleImage;
    MultipartFile roleImageFile;



}
