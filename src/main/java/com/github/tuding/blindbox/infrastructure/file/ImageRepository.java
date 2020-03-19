package com.github.tuding.blindbox.infrastructure.file;

import com.github.tuding.blindbox.domain.ImageCategory;
import com.github.tuding.blindbox.exception.BizException;
import com.github.tuding.blindbox.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Repository
@Slf4j
public class ImageRepository {

    @Value("${app.imagePath}")
    private String imagePath;

    public String saveImage(String fileName, ImageCategory imageCategory, MultipartFile file) {
        try {
            File storeFile = new File(getBasePath(imageCategory) + fileName + ".png");
            file.transferTo(storeFile);
            return storeFile.getCanonicalPath();
        } catch (IOException e) {
            log.error("Failed to save image " + fileName, e);
            throw new BizException(ErrorCode.FAIL_TO_STORE_FILE);
        }
    }


    String getBasePath(ImageCategory imageCategory) {
        switch (imageCategory) {
            case ACTIVITY:
                return imagePath + "/activities/";
            case ROLE:
                return imagePath + "/roles/";
            case SERIES:
                return imagePath + "/series/";
            case PRODUCT:
                return imagePath + "/product/";
            default:
                return imagePath + "/";
        }
    }
}
