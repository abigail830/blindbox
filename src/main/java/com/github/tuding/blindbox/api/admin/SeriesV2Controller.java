package com.github.tuding.blindbox.api.admin;

import com.github.tuding.blindbox.api.admin.dto.RoleWithCheck;
import com.github.tuding.blindbox.api.admin.dto.SeriesDTO;
import com.github.tuding.blindbox.api.admin.dto.SeriesV2DTO;
import com.github.tuding.blindbox.domain.ImageCategory;
import com.github.tuding.blindbox.domain.product.Role;
import com.github.tuding.blindbox.domain.product.Series;
import com.github.tuding.blindbox.exception.BizException;
import com.github.tuding.blindbox.exception.ErrorCode;
import com.github.tuding.blindbox.exception.SeriesNotFoundException;
import com.github.tuding.blindbox.infrastructure.file.ImageRepository;
import com.github.tuding.blindbox.infrastructure.repository.RolesRepository;
import com.github.tuding.blindbox.infrastructure.repository.SeriesRespository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/admin-ui/series/v2")
public class SeriesV2Controller {

    private static final ThreadLocal<SimpleDateFormat> simpleDateFormatter
            = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));

    @Autowired
    SeriesRespository seriesRespository;

    @Autowired
    RolesRepository rolesRepository;

    @Autowired
    ImageRepository imageRepository;
    @Value("${app.imagePath}")
    private String imagePath;

    @GetMapping("/")
    public String seriesPage(Model model) {
        List<Series> seriesList = seriesRespository.queryAllSeries();
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

        final SeriesV2DTO series = seriesRespository.querySeriesV2ByID(id).map(SeriesV2DTO::new)
                .orElseThrow(() -> new BizException(ErrorCode.WX_USER_NOT_FOUND));
        final List<String> linkedRoleIds = seriesRespository.queryLinkedRoleIdsBySeriesId(id);
        series.setLinkedRoleIds(linkedRoleIds);
        log.info("{}", series);

        final List<RoleWithCheck> roles = rolesRepository.queryRolesOrderByName()
                .stream().map(role -> {
                    if (series.isContainedRole(role.getId()))
                        return new RoleWithCheck(role, Boolean.TRUE);
                    else
                        return new RoleWithCheck(role, Boolean.FALSE);
                }).collect(Collectors.toList());

        model.addAttribute("roles", roles);
        model.addAttribute("series", series);
        log.info("Edit seriesV2DTO [{}]", series);

        return "seriesform_v2";
    }

    @PostMapping("/form")
    public RedirectView handleForm(@RequestParam("role") List<String> roleList,
                                   @ModelAttribute("series") SeriesV2DTO seriesDTO,
                                   Model model) throws IOException, ParseException {
        seriesDTO.setLinkedRoleIds(roleList);
        log.info("-------- {}", seriesDTO);

        if (StringUtils.isNotBlank(seriesDTO.getId())) {
            updateSeries(seriesDTO);
        } else {
            createSeries(seriesDTO);
        }
        return new RedirectView("/admin-ui/series/v2/");
    }

    private void createSeries(@ModelAttribute("series") SeriesV2DTO seriesDTO) {
        UUID seriesID = UUID.randomUUID();
        log.info("handle series creation as {} id {}", seriesDTO, seriesID.toString());

        String image = imageRepository.saveImage(seriesID.toString(), ImageCategory.SERIES, seriesDTO.getSeriesImageFile());
        seriesDTO.setSeriesImage(image);
        image = imageRepository.saveImage(seriesID.toString() + "-matrixHeaderImage", ImageCategory.SERIES, seriesDTO.getMatrixHeaderImageFile());
        seriesDTO.setMatrixHeaderImage(image);
        image = imageRepository.saveImage(seriesID.toString() + "-matrixCellImage", ImageCategory.SERIES, seriesDTO.getMatrixCellImageFile());
        seriesDTO.setMatrixCellImage(image);
        image = imageRepository.saveImage(seriesID.toString() + "-longImage", ImageCategory.SERIES, seriesDTO.getLongImageFile());
        seriesDTO.setLongImage(image);
        image = imageRepository.saveImage(seriesID.toString() + "-boxImage", ImageCategory.SERIES, seriesDTO.getBoxImageFile());
        seriesDTO.setBoxImage(image);
        image = imageRepository.saveImage(seriesID.toString() + "-posterBgImage", ImageCategory.SERIES, seriesDTO.getPosterBgImageFile());
        seriesDTO.setPosterBgImage(image);

        seriesDTO.setId(seriesID.toString());
        seriesRespository.createSeriesV2(seriesDTO.toDomainObject());
    }

    private void updateSeries(@ModelAttribute("series") SeriesV2DTO seriesDTO) {
        log.info("handle series update as {} ", seriesDTO);

        if (seriesDTO.getSeriesImageFile().getSize() > 0) {
            String image = imageRepository.saveImage(seriesDTO.getId(), ImageCategory.SERIES, seriesDTO.getSeriesImageFile());
            seriesDTO.setSeriesImage(image);
        }

        if (seriesDTO.getMatrixHeaderImageFile().getSize() > 0) {
            String image = imageRepository.saveImage(seriesDTO.getId() + "-matrixHeaderImage", ImageCategory.SERIES, seriesDTO.getMatrixHeaderImageFile());
            seriesDTO.setMatrixHeaderImage(image);
        }

        if (seriesDTO.getMatrixCellImageFile().getSize() > 0) {
            String image = imageRepository.saveImage(seriesDTO.getId() + "-matrixCellImage", ImageCategory.SERIES, seriesDTO.getMatrixCellImageFile());
            seriesDTO.setMatrixCellImage(image);
        }

        if (seriesDTO.getLongImageFile().getSize() > 0) {
            String image = imageRepository.saveImage(seriesDTO.getId() + "-longImage", ImageCategory.SERIES, seriesDTO.getLongImageFile());
            seriesDTO.setLongImage(image);
        }

        if (seriesDTO.getBoxImageFile().getSize() > 0) {
            String image = imageRepository.saveImage(seriesDTO.getId() + "-boxImage", ImageCategory.SERIES, seriesDTO.getBoxImageFile());
            seriesDTO.setBoxImage(image);
        } else {
            seriesDTO.setBoxImage(imageRepository.getPath(seriesDTO.getId() + "-boxImage", ImageCategory.SERIES));
        }
        if (seriesDTO.getPosterBgImageFile().getSize() > 0) {
            String image = imageRepository.saveImage(seriesDTO.getId() + "-posterBgImage", ImageCategory.SERIES, seriesDTO.getPosterBgImageFile());
            seriesDTO.setPosterBgImage(image);
        }

        seriesRespository.updateSeriesV2(seriesDTO.toDomainObject());
    }

    @GetMapping("/role/{id}")
    public List<SeriesDTO> getSeriesList(@PathVariable("id") String roleID) {
        Optional<Role> roleOptional = rolesRepository.queryRolesByID(roleID);
        if (roleOptional.isPresent()) {
            List<Series> series = seriesRespository.queryByRoleID(roleOptional.get().getId());
            return series.stream().map(SeriesDTO::new).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @GetMapping("/{id}")
    public SeriesDTO getSeries(@PathVariable("id") String id) {
        Optional<Series> seriesOptional = seriesRespository.querySeriesByID(id);
        if (seriesOptional.isPresent()) {
            return new SeriesDTO(seriesOptional.get());
        } else {
            throw new SeriesNotFoundException();
        }
    }

    @DeleteMapping("/{id}")
    public @ResponseBody
    void deleteSeries(@PathVariable("id") String id) {
        seriesRespository.deleteSeries(id);
    }


    @GetMapping("/{id}/images")
    public ResponseEntity<Resource> getSeriesImage(@PathVariable("id") String id) throws FileNotFoundException {
        Optional<Series> seriesOptional = seriesRespository.querySeriesByID(id);
        if (seriesOptional.isPresent()) {
            File file = new File(seriesOptional.get().getSeriesImage());
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("image/png"))
                    .body(resource);
        } else {
            throw new SeriesNotFoundException();
        }
    }

    @GetMapping("/{id}/headerimage")
    public ResponseEntity<Resource> getSeriesHeaderImage(@PathVariable("id") String id) throws FileNotFoundException {
        Optional<Series> seriesOptional = seriesRespository.querySeriesByID(id);
        if (seriesOptional.isPresent()) {
            File file = new File(seriesOptional.get().getMatrixHeaderImage());
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("image/png"))
                    .body(resource);
        } else {
            throw new SeriesNotFoundException();
        }
    }

    @GetMapping("/{id}/cellimage")
    public ResponseEntity<Resource> getSeriesCellImage(@PathVariable("id") String id) throws FileNotFoundException {
        Optional<Series> seriesOptional = seriesRespository.querySeriesByID(id);
        if (seriesOptional.isPresent()) {
            File file = new File(seriesOptional.get().getMatrixCellImage());
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("image/png"))
                    .body(resource);
        } else {
            throw new SeriesNotFoundException();
        }
    }

}
