package com.github.tuding.blindbox.api.admin;

import com.github.tuding.blindbox.api.admin.dto.RoleDTO;
import com.github.tuding.blindbox.api.admin.dto.SeriesDTO;
import com.github.tuding.blindbox.domain.ImageCategory;
import com.github.tuding.blindbox.domain.Role;
import com.github.tuding.blindbox.domain.Series;
import com.github.tuding.blindbox.exception.RolesNotFoundException;
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
@RequestMapping("/admin-ui/series")
public class SeriesController {

    private static final ThreadLocal<SimpleDateFormat> simpleDateFormatter
            = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));

    @Autowired
    SeriesRespository seriesRespository;

    @Autowired
    RolesRepository rolesRepository;

    @Autowired
    ImageRepository imageRepository;

    @GetMapping("/")
    public String seriesPage(Model model,
                             @RequestParam("roleID") String roleID) {

        List<Series> seriesList = seriesRespository.queryByRoleID(roleID);
        List<SeriesDTO> seriesDTOs = seriesList.stream().map(SeriesDTO::new).collect(Collectors.toList());
        model.addAttribute("series", seriesDTOs);
        model.addAttribute("role", new RoleDTO(rolesRepository.queryRolesByID(roleID).get()));
        return "series";
    }

    @GetMapping("/seriesform")
    public String createForm(Model model,
                             @RequestParam("roleId") String roleId) {
        model.addAttribute("roleId", roleId);
        model.addAttribute("series", new SeriesDTO());

        return "seriesform";
    }

    @GetMapping("/seriesform/{id}")
    public String editRole(Model model, @PathVariable String id) {

        final Optional<Series> seriesOptional = seriesRespository.querySeriesByID(id);
        seriesOptional.ifPresent(series -> {
                    model.addAttribute("roleId", series.getRoleId());
                    model.addAttribute("series", new SeriesDTO(series));
                    log.info("Edit seriesDTO [{}]", series);
                }
        );
        return "seriesform";
    }


    @Value("${app.imagePath}")
    private String imagePath;

    @PostMapping("/form/{roleID}")
    public RedirectView handleForm(@PathVariable("roleID") String roleId,
                                   @ModelAttribute("roleForm") SeriesDTO seriesDTO,
                                   Model model) throws IOException, ParseException {
        Optional<Role> roleOptional = rolesRepository.queryRolesByID(roleId);
        if (roleOptional.isPresent()) {
            if (StringUtils.isNotBlank(seriesDTO.getId())) {
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
                    seriesDTO.setBoxImage(imageRepository.getPath(seriesDTO.getId() + "-boxImage",ImageCategory.SERIES));
                }

                seriesDTO.setRoleId(roleId);
                seriesRespository.updateSeries(seriesDTO.toDomainObject());

                return new RedirectView("/admin-ui/series/?roleID=" + roleOptional.get().getId());

            } else {
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

                seriesDTO.setId(seriesID.toString());
                seriesDTO.setRoleId(roleId);
                seriesRespository.createSeries(seriesDTO.toDomainObject());
                return new RedirectView("/admin-ui/series/?roleID=" + roleOptional.get().getId());
            }
        } else {
            throw new RolesNotFoundException();
        }

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
