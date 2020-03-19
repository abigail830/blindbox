package com.github.tuding.blindbox.api.admin;

import com.github.tuding.blindbox.api.admin.dto.RoleDTO;
import com.github.tuding.blindbox.api.admin.dto.SeriesDTO;
import com.github.tuding.blindbox.domain.ImageCategory;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

        List<SeriesDTO> seriesDTOs = seriesRespository.queryByRoleID(roleID);
        model.addAttribute("series", seriesDTOs);
        model.addAttribute("role", rolesRepository.queryRolesByID(roleID).get());
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

        final Optional<SeriesDTO> seriesDTOOptional = seriesRespository.querySeriesByID(id);
        seriesDTOOptional.ifPresent(roleDTO -> {
                    model.addAttribute("roleId", seriesDTOOptional.get().getRoleId());
                    model.addAttribute("series", seriesDTOOptional.get());
                    log.info("Edit seriesDTO [{}]", seriesDTOOptional.get());
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
        Optional<RoleDTO> roleDTO = rolesRepository.queryRolesByID(roleId);
        if (roleDTO.isPresent()) {
            if (StringUtils.isNotBlank(seriesDTO.getId())) {
                log.info("handle role update as {} ", roleDTO);

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
                seriesDTO.setRoleId(roleId);
                seriesRespository.updateSeries(seriesDTO);

                return new RedirectView("/admin-ui/series/?roleID=" + roleDTO.get().getId());

            } else {
                UUID seriesID = UUID.randomUUID();
                log.info("handle role creation as {} id {}", roleDTO, seriesID.toString());

                String image = imageRepository.saveImage(seriesDTO.getId(), ImageCategory.SERIES, seriesDTO.getSeriesImageFile());
                seriesDTO.setSeriesImage(image);
                image = imageRepository.saveImage(seriesDTO.getId() + "-matrixHeaderImage", ImageCategory.SERIES, seriesDTO.getMatrixHeaderImageFile());
                seriesDTO.setMatrixHeaderImage(image);
                image = imageRepository.saveImage(seriesDTO.getId() + "-matrixCellImage", ImageCategory.SERIES, seriesDTO.getMatrixCellImageFile());
                seriesDTO.setMatrixCellImage(image);
                seriesDTO.setId(seriesID.toString());
                seriesDTO.setRoleId(roleId);
                seriesRespository.createSeries(seriesDTO);
                return new RedirectView("/admin-ui/series/?roleID=" + roleDTO.get().getId());
            }
        } else {
            throw new RolesNotFoundException();
        }

    }

    @GetMapping("/role/{id}")
    public List<SeriesDTO> getSeriesList(@PathVariable("id") String roleID) {
        Optional<RoleDTO> roleDTO = rolesRepository.queryRolesByID(roleID);
        if (roleDTO.isPresent()) {
            return seriesRespository.queryByRoleID(roleDTO.get().getId());
        } else {
            return Collections.emptyList();
        }
    }

    @GetMapping("/{id}")
    public SeriesDTO getSeries(@PathVariable("id") String id) {
        Optional<SeriesDTO> seriesDTOOptional = seriesRespository.querySeriesByID(id);
        if (seriesDTOOptional.isPresent()) {
            return seriesDTOOptional.get();
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
        Optional<SeriesDTO> seriesDTOOptional = seriesRespository.querySeriesByID(id);
        if (seriesDTOOptional.isPresent()) {
            File file = new File(seriesDTOOptional.get().getSeriesImage());
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
        Optional<SeriesDTO> seriesDTOOptional = seriesRespository.querySeriesByID(id);
        if (seriesDTOOptional.isPresent()) {
            File file = new File(seriesDTOOptional.get().getMatrixHeaderImage());
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
        Optional<SeriesDTO> seriesDTOOptional = seriesRespository.querySeriesByID(id);
        if (seriesDTOOptional.isPresent()) {
            File file = new File(seriesDTOOptional.get().getMatrixCellImage());
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
