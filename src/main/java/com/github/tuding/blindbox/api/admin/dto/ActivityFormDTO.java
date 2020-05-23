package com.github.tuding.blindbox.api.admin.dto;

import com.github.tuding.blindbox.domain.activity.Activity;
import com.github.tuding.blindbox.infrastructure.Constant;
import com.google.common.base.Strings;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
@Slf4j
public class ActivityFormDTO {

    String id;
    String name;
    String description;
    MultipartFile mainImg;
    MultipartFile contentImg;
    Boolean shownInAd;
    String activityStartDate;
    String activityEndDate;

    String mainImgAddr;
    String contentImgAddr;

    Integer giftBonus;

    Boolean readOnly;
    String title;
    String mode;
    String errorMsg;

    private static final ThreadLocal<SimpleDateFormat> dateFormat
            = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd kk:mm"));

    public ActivityFormDTO(Mode mode) {
        setupMode(mode);
    }

    public ActivityFormDTO(Activity activity, Mode mode) {
        this.id = activity.getId();
        this.name = activity.getActivityName();
        this.description = Strings.isNullOrEmpty(activity.getActivityDescription()) ? "" : activity.getActivityDescription();
        this.shownInAd = activity.getShownInAd();
        this.mainImgAddr = Constant.ADMIN_UI_IMAGE_PATH + activity.getMainImgAddr();
        this.contentImgAddr = Constant.ADMIN_UI_IMAGE_PATH + activity.getContentImgAddr();
        this.activityStartDate = dateFormat.get().format(activity.getActivityStartDate());
        this.activityEndDate = dateFormat.get().format(activity.getActivityEndDate());
        this.giftBonus = activity.getGiftBonus();

        setupMode(mode);

    }

    public Timestamp getActivityStartDateAsTS() {
        return Timestamp.valueOf(this.activityStartDate + ":00");
    }

    public Timestamp getActivityEndDateAsTS() {
        return Timestamp.valueOf(this.activityEndDate + ":00");
    }

    public Activity toActivity() {
        log.info("{}", toString());
        return new Activity(id, name, description, shownInAd,
                getActivityStartDateAsTS(), getActivityEndDateAsTS(),
                mainImg, mainImgAddr.replace(Constant.ADMIN_UI_IMAGE_PATH, ""),
                contentImg, contentImgAddr.replace(Constant.ADMIN_UI_IMAGE_PATH, ""), giftBonus);
    }

    public void setupMode(Mode mode) {
        if (mode.equals(Mode.ADD)) {
            this.readOnly = Boolean.FALSE;
            this.title = "添加活动";
        } else if (mode.equals(Mode.EDIT)) {
            this.readOnly = Boolean.FALSE;
            this.title = "编辑活动";
        } else {
            this.readOnly = Boolean.TRUE;
            this.title = "查看活动";
        }
        this.mode = mode.name();
    }
}
