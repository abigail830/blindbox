package com.github.tuding.blindbox.api.wx;

import com.github.tuding.blindbox.exception.ImageNotFoundException;
import com.github.tuding.blindbox.filter.NeedWxVerifyToken;
import com.github.tuding.blindbox.infrastructure.file.ImageRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RestController
@RequestMapping("/wx/img")
@Slf4j
@Api(value = "图片资源接口", description = "图片资源接口")
public class WxImageController {
    @Autowired
    ImageRepository imageRepository;

    @GetMapping("/")
    @NeedWxVerifyToken
    @ApiOperation(value = "根据路径获取图片(需要带token", notes = "其他接口涉及图片的地方会拼装好这里所需的path")
    public ResponseEntity<Resource> getImage(@RequestParam("path") String path) throws FileNotFoundException {
        File image = imageRepository.getImage(path);
        if (image.exists()) {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(image));
            return ResponseEntity.ok()
                    .contentLength(image.length())
                    .contentType(MediaType.parseMediaType("image/png"))
                    .body(resource);
        } else {
            log.error("Failed to find image for " + path);
            throw new ImageNotFoundException();
        }
    }
}
