package com.github.tuding.blindbox.api.admin;

import com.github.tuding.blindbox.exception.ImageNotFoundException;
import com.github.tuding.blindbox.filter.NeedWxVerifyToken;
import com.github.tuding.blindbox.infrastructure.file.ImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Slf4j
@Controller
@RequestMapping("/admin-ui/image")
public class ImageController {
    @Autowired
    ImageRepository imageRepository;

    @GetMapping("/")
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
