package com.github.tuding.blindbox.api.admin;

import com.github.tuding.blindbox.api.admin.dto.ActivityBriefDTO;
import com.github.tuding.blindbox.api.admin.dto.ActivityFormDTO;
import com.github.tuding.blindbox.api.admin.dto.Mode;
import com.github.tuding.blindbox.domain.Activity;
import com.github.tuding.blindbox.domain.ActivityService;
import com.github.tuding.blindbox.infrastructure.file.ImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

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
        //TODO: to query all activities in brief format

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
    String deleteActivity(@PathVariable String id) {
        activityService.deleteActivityById(id);
        log.info("Deleted activity {}", id);
        return "redirect:/admin-ui/activities/";
    }

    @GetMapping("/form")
    public String addAcvitityPage(Model model) {
        model.addAttribute("activityForm", new ActivityFormDTO(Mode.ADD));
        log.info("Going to create activity");
        return "activityForm";
    }

    @PostMapping("/form")
    public RedirectView handleForm(@ModelAttribute("activityForm") ActivityFormDTO activityForm) throws IOException {
        log.info("handle activity form creation [{}]", activityForm);
        //TODO: image format/size check

        final Activity activity = activityForm.toActivity();
        activityService.saveActivity(activity);

        return new RedirectView("/admin-ui/activities/");
    }

}
