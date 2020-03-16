package com.github.tuding.blindbox.api.admin;

import com.github.tuding.blindbox.api.admin.dto.ActivityFormDTO;
import com.github.tuding.blindbox.domain.Activity;
import com.github.tuding.blindbox.domain.ActivityService;
import com.github.tuding.blindbox.infrastructure.file.ImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.ArrayList;

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

        model.addAttribute("activities", new ArrayList<>());
        return "activity";
    }

    @GetMapping("/form")
    public String addAcvitityPage(Model model) {
        model.addAttribute("activityForm", new ActivityFormDTO());
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
