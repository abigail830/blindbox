package com.github.tuding.blindbox.api.admin;

import com.github.tuding.blindbox.api.admin.dto.ActivityBriefDTO;
import com.github.tuding.blindbox.api.admin.dto.ActivityFormDTO;
import com.github.tuding.blindbox.api.admin.dto.Mode;
import com.github.tuding.blindbox.domain.Activity;
import com.github.tuding.blindbox.domain.ActivityService;
import com.github.tuding.blindbox.exception.ActivityImgNotFoundException;
import com.github.tuding.blindbox.infrastructure.file.ImageRepository;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin-ui/activities")
@Slf4j
public class ActivityController {

    @Autowired
    ActivityService activityService;

    @Autowired
    ImageRepository imageRepository;

    @GetMapping("/")
    public String homepage(Model model) {
        final List<ActivityBriefDTO> activityBriefDTOS = activityService.getAllActivities().stream()
                .map(ActivityBriefDTO::new)
                .collect(Collectors.toList());
        model.addAttribute("activities", activityBriefDTOS);
        return "activity";
    }

    @GetMapping("/id/{id}")
    public String viewActivityDetail(Model model, @PathVariable String id) {
        final Optional<Activity> activityById = activityService.getActivityById(id);
        activityById.ifPresent(activity -> {
                    model.addAttribute("activityForm", new ActivityFormDTO(activity, Mode.READ));
                    log.info("View activity [{}]", activity);
                }
        );
        return "activityForm";
    }

    @GetMapping("/edit/id/{id}")
    public String editActivityDetail(Model model, @PathVariable String id) {

        final Optional<Activity> activityById = activityService.getActivityById(id);
        activityById.ifPresent(activity -> {
                    model.addAttribute("activityForm", new ActivityFormDTO(activity, Mode.EDIT));
                    log.info("Edit activity [{}]", activity);
                }
        );
        return "activityForm";
    }

    @DeleteMapping("/id/{id}")
    public @ResponseBody
    void deleteActivity(@PathVariable String id) {
        activityService.deleteActivityById(id);
        log.info("Deleted activity {}", id);
//        return "redirect:/admin-ui/activities/";
    }

    @GetMapping("/form")
    public String addAcvitityPage(Model model) {
        model.addAttribute("activityForm", new ActivityFormDTO(Mode.ADD));
        log.info("Going to show activity form");
        return "activityForm";
    }

    @PostMapping("/form")
    public String handleForm(@ModelAttribute("activityForm") ActivityFormDTO activityForm, Model model) throws IOException {
        log.info("handle activity form creation [{}]", activityForm);

        if (activityForm.getMainImg().isEmpty() && Strings.isNullOrEmpty(activityForm.getMainImgAddr())) {
            activityForm.setupMode(Mode.valueOf(activityForm.getMode()));
            activityForm.setErrorMsg("活动主图缺失！");
            log.info("Lack of main image, redirect back to form [{}]", activityForm);
            model.addAttribute("activityForm", activityForm);
            return "activityForm";
        }

        if (activityForm.getActivityStartDateAsTS().after(activityForm.getActivityEndDateAsTS())) {
            activityForm.setupMode(Mode.valueOf(activityForm.getMode()));
            activityForm.setErrorMsg("活动开始日期不能晚于结束日期");
            log.info("Activity start date < end date, redirect back to form [{}]", activityForm);
            model.addAttribute("activityForm", activityForm);
            return "activityForm";
        }

        final Activity activity = activityForm.toActivity();
        log.info("activity {}", activity);

        if (activity.getId() == null) {
            activityService.saveActivity(activity);
        } else {
            activityService.updateActivity(activity);
        }

        return "redirect:/admin-ui/activities/";
    }

    @GetMapping("/id/{id}/mainImg")
    public ResponseEntity<Resource> getMainImage(@PathVariable("id") String id) throws FileNotFoundException {
        Optional<Activity> activity = activityService.getActivityById(id);
        if (activity.isPresent()) {
            File file = new File(activity.get().getMainImgAddr());
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("image/png"))
                    .body(resource);
        } else {
            throw new ActivityImgNotFoundException();
        }
    }


}
