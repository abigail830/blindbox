package com.github.tuding.blindbox.api.admin;

import com.github.tuding.blindbox.api.admin.dto.RoleWithCheck;
import com.github.tuding.blindbox.api.admin.dto.SeriesV2DTO;
import com.github.tuding.blindbox.domain.ImageCategory;
import com.github.tuding.blindbox.domain.product.Series;
import com.github.tuding.blindbox.exception.BizException;
import com.github.tuding.blindbox.exception.ErrorCode;
import com.github.tuding.blindbox.exception.SeriesNotFoundException;
import com.github.tuding.blindbox.infrastructure.file.ImageRepository;
import com.github.tuding.blindbox.infrastructure.repository.RolesRepository;
import com.github.tuding.blindbox.infrastructure.repository.SeriesRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/admin-ui/series/v2")
public class SeriesV2Controller {

    @Autowired
    SeriesRepository seriesRepository;

    @Autowired
    RolesRepository rolesRepository;

    @Autowired
    ImageRepository imageRepository;
    @Value("${app.imagePath}")
    private String imagePath;

    @GetMapping("/")
    public String seriesPage(Model model) {
        List<Series> seriesList = seriesRepository.queryAllSeriesWithoutRoleIds();
        List<SeriesV2DTO> seriesDTOs = seriesList.stream().map(SeriesV2DTO::new).collect(Collectors.toList());
        model.addAttribute("series", seriesDTOs);
        return "series_v2";
    }

    @GetMapping("/seriesform")
    public String createForm(Model model) {
        final List<RoleWithCheck> roles = rolesRepository.queryRolesOrderByName()
                .stream().map(RoleWithCheck::new).collect(Collectors.toList());
        model.addAttribute("roles", roles);
        model.addAttribute("series", new SeriesV2DTO());
        return "seriesform_v2";
    }

    @GetMapping("/seriesform/{id}")
    public String editRole(Model model, @PathVariable String id) {
        // construct series
        final SeriesV2DTO series = seriesRepository.querySeriesByIDWithRoleIds(id).map(SeriesV2DTO::new)
                .orElseThrow(() -> new BizException(ErrorCode.WX_USER_NOT_FOUND));
        final List<String> linkedRoleIds = seriesRepository.queryLinkedRoleIdsBySeriesId(id);
        series.setRoleIds(linkedRoleIds);

        // construct roles
        final List<RoleWithCheck> roles = rolesRepository.queryRolesOrderByName()
                .stream().map(role -> {
                    if (series.isContainedRole(role.getId()))
                        return new RoleWithCheck(role, Boolean.TRUE);
                    else
                        return new RoleWithCheck(role, Boolean.FALSE);
                }).collect(Collectors.toList());

        model.addAttribute("roles", roles);
        model.addAttribute("series", series);

        log.debug("Edit seriesV2DTO [{}]", series);
        return "seriesform_v2";
    }

    @PostMapping("/form")
    public RedirectView handleForm(@RequestParam(value = "role", required = false) List<String> roleList,
                                   @ModelAttribute("series") SeriesV2DTO seriesDTO,
                                   Model model) throws IOException, ParseException {
        if (roleList != null && !roleList.isEmpty())
            seriesDTO.setNewlinkedRoleIds(roleList);
        log.debug("-------- {}", seriesDTO);

        if (StringUtils.isNotBlank(seriesDTO.getId())) {
            updateSeries(seriesDTO);
        } else {
            createSeries(seriesDTO);
        }
        return new RedirectView("/admin-ui/series/v2/");
    }

    @Transactional
    private void createSeries(@ModelAttribute("series") SeriesV2DTO seriesDTO) {
        UUID seriesID = UUID.randomUUID();
        log.info("handle series creation as {} id {}", seriesDTO, seriesID.toString());

        String image = imageRepository.saveImage(seriesID.toString(), ImageCategory.SERIES, seriesDTO.getSeriesImageFile());
        seriesDTO.setSeriesImage(image + "?ts=" + System.currentTimeMillis()/1000);
        image = imageRepository.saveImage(seriesID.toString() + "-matrixHeaderImage", ImageCategory.SERIES, seriesDTO.getMatrixHeaderImageFile());
        seriesDTO.setMatrixHeaderImage(image + "?ts=" + System.currentTimeMillis()/1000);
        image = imageRepository.saveImage(seriesID.toString() + "-matrixCellImage", ImageCategory.SERIES, seriesDTO.getMatrixCellImageFile());
        seriesDTO.setMatrixCellImage(image + "?ts=" + System.currentTimeMillis()/1000);
        image = imageRepository.saveImage(seriesID.toString() + "-longImage", ImageCategory.SERIES, seriesDTO.getLongImageFile());
        seriesDTO.setLongImage(image + "?ts=" + System.currentTimeMillis()/1000);
        image = imageRepository.saveImage(seriesID.toString() + "-boxImage", ImageCategory.SERIES, seriesDTO.getBoxImageFile());
        seriesDTO.setBoxImage(image + "?ts=" + System.currentTimeMillis()/1000);
        image = imageRepository.saveImage(seriesID.toString() + "-posterBgImage", ImageCategory.SERIES, seriesDTO.getPosterBgImageFile());
        seriesDTO.setPosterBgImage(image + "?ts=" + System.currentTimeMillis()/1000);

        seriesDTO.setId(seriesID.toString());
        seriesRepository.createSeriesV2(seriesDTO.toDomainObject());
        seriesRepository.addSeriesRoleMappingV2(seriesDTO.getId(), seriesDTO.getNewlinkedRoleIds());
    }

    @Transactional
    private void updateSeries(@ModelAttribute("series") SeriesV2DTO seriesDTO) {
        log.info("handle series update as {} ", seriesDTO);
        updateSeriesImages(seriesDTO);
        seriesRepository.updateSeriesV2(seriesDTO.toDomainObject());

        final List<String> toBeRemove = seriesDTO.getOrilinkedRoleIds().stream()
                .filter(role -> !seriesDTO.getNewlinkedRoleIds().contains(role)).collect(Collectors.toList());
        log.info("toBeRemove: {}", toBeRemove);
        if (!toBeRemove.isEmpty())
            seriesRepository.removeSeriesRoleMapping(seriesDTO.getId(), toBeRemove);

        final List<String> toBeAdd = seriesDTO.getNewlinkedRoleIds().stream()
                .filter(newRole -> !seriesDTO.getOrilinkedRoleIds().contains(newRole)).collect(Collectors.toList());
        log.info("toBeAdd {}", toBeAdd);
        if (!toBeAdd.isEmpty())
            seriesRepository.addSeriesRoleMappingV2(seriesDTO.getId(), toBeAdd);
    }

    private void updateSeriesImages(@ModelAttribute("series") SeriesV2DTO seriesDTO) {
        if (seriesDTO.getSeriesImageFile().getSize() > 0) {
            String image = imageRepository.saveImage(seriesDTO.getId(), ImageCategory.SERIES, seriesDTO.getSeriesImageFile());
            seriesDTO.setSeriesImage(image + "?ts=" + System.currentTimeMillis()/1000);
        }

        if (seriesDTO.getMatrixHeaderImageFile().getSize() > 0) {
            String image = imageRepository.saveImage(seriesDTO.getId() + "-matrixHeaderImage", ImageCategory.SERIES, seriesDTO.getMatrixHeaderImageFile());
            seriesDTO.setMatrixHeaderImage(image + "?ts=" + System.currentTimeMillis()/1000);
        }

        if (seriesDTO.getMatrixCellImageFile().getSize() > 0) {
            String image = imageRepository.saveImage(seriesDTO.getId() + "-matrixCellImage", ImageCategory.SERIES, seriesDTO.getMatrixCellImageFile());
            seriesDTO.setMatrixCellImage(image + "?ts=" + System.currentTimeMillis()/1000);
        }

        if (seriesDTO.getLongImageFile().getSize() > 0) {
            String image = imageRepository.saveImage(seriesDTO.getId() + "-longImage", ImageCategory.SERIES, seriesDTO.getLongImageFile());
            seriesDTO.setLongImage(image + "?ts=" + System.currentTimeMillis()/1000);
        }

        if (seriesDTO.getBoxImageFile().getSize() > 0) {
            String image = imageRepository.saveImage(seriesDTO.getId() + "-boxImage", ImageCategory.SERIES, seriesDTO.getBoxImageFile());
            seriesDTO.setBoxImage(image + "?ts=" + System.currentTimeMillis()/1000);
        } else {
            seriesDTO.setBoxImage(imageRepository.getPath(seriesDTO.getId() + "-boxImage", ImageCategory.SERIES));
        }
        if (seriesDTO.getPosterBgImageFile().getSize() > 0) {
            String image = imageRepository.saveImage(seriesDTO.getId() + "-posterBgImage", ImageCategory.SERIES, seriesDTO.getPosterBgImageFile());
            seriesDTO.setPosterBgImage(image + "?ts=" + System.currentTimeMillis()/1000);
        }
    }

    @GetMapping("/{id}")
    public SeriesV2DTO getSeries(@PathVariable("id") String id) {
        Optional<Series> seriesOptional = seriesRepository.querySeriesByIDWithoutRoleIds(id);
        if (seriesOptional.isPresent()) {
            return new SeriesV2DTO(seriesOptional.get());
        } else {
            throw new SeriesNotFoundException();
        }
    }

    @DeleteMapping("/{id}")
    public @ResponseBody
    void deleteSeries(@PathVariable("id") String id) {
        seriesRepository.deleteSeries(id);
    }

}
