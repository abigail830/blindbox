package com.github.tuding.blindbox.api.admin;

import com.github.tuding.blindbox.api.admin.dto.RoleDTO;
import com.github.tuding.blindbox.api.admin.dto.SeriesDTO;
import com.github.tuding.blindbox.exception.RolesNotFoundException;
import com.github.tuding.blindbox.exception.SeriesNotFoundException;
import com.github.tuding.blindbox.infrastructure.repository.RolesRepository;
import com.github.tuding.blindbox.infrastructure.repository.SeriesRespository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

@Slf4j
@RestController
@RequestMapping("/series")
public class SeriesHandler {
    private static final ThreadLocal<SimpleDateFormat> simpleDateFormatter
            = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));

    @Autowired
    SeriesRespository seriesRespository;

    @Autowired
    RolesRepository rolesRepository;

    @Value("${app.imagePath}")
    private String imagePath;

    @PostMapping("/{roleName}")
    public RedirectView handleForm(@PathVariable("roleName") String roleName,
                                   @RequestParam("name") String name,
                                   @RequestParam("releaseDate") String releaseDate,
                                   @RequestParam("isNewSeries") boolean isNewSeries,
                                   @RequestParam("isPresale") boolean isPresale,
                                   @RequestParam("price") BigDecimal price,
                                   @RequestParam("seriesImage") MultipartFile seriesImage,
                                   @RequestParam("matrixHeaderImage") MultipartFile matrixHeaderImage,
                                   @RequestParam("matrixCellImage") MultipartFile matrixCellImage) throws IOException, ParseException {
        log.info("handle series creation as role name {} series name {} release date {} " +
                " isNewSeries {} isPresale {} price {} ", roleName, name, releaseDate, isNewSeries, isPresale, price);
        Optional<RoleDTO> roleDTO = rolesRepository.queryRolesByName(roleName);
        if(roleDTO.isPresent()) {
            File seriesImageFile = new File(getSeriesFolder(name) + "image" + ".png");
            seriesImage.transferTo(seriesImageFile);
            File matrixHeaderImageFile = new File(getSeriesFolder(name) + "matrixHeaderImage" + ".png");
            matrixHeaderImage.transferTo(matrixHeaderImageFile);
            File matrixCellImageFile = new File(getSeriesFolder(name) + "matrixCellImage" + ".png");
            matrixCellImage.transferTo(matrixCellImageFile);

            SeriesDTO seriesDTO = new SeriesDTO();
            seriesDTO.setRoleId(roleDTO.get().getId());
            seriesDTO.setName(name);
            seriesDTO.setReleaseDate(simpleDateFormatter.get().parse(releaseDate));
            seriesDTO.setNewSeries(isNewSeries);
            seriesDTO.setPresale(isPresale);
            seriesDTO.setPrice(price);
            seriesDTO.setSeriesImage(seriesImageFile.getCanonicalPath());
            seriesDTO.setMatrixHeaderImage(matrixHeaderImageFile.getCanonicalPath());
            seriesDTO.setMatrixCellImage(matrixCellImageFile.getCanonicalPath());
            seriesRespository.createSeries(seriesDTO);
            return new RedirectView("/admin-ui/series/?roleID=" + roleDTO.get().getId());

        } else {
            throw new RolesNotFoundException();
        }

    }

    @GetMapping("/role/{name}")
    public List<SeriesDTO> getSeriesList(@PathVariable("name")String roleName) {
        Optional<RoleDTO> roleDTO = rolesRepository.queryRolesByName(roleName);
        if (roleDTO.isPresent()) {
            return seriesRespository.queryByRoleID(roleDTO.get().getId());
        } else {
            return Collections.emptyList();
        }
    }

    @GetMapping("/{name}")
    public SeriesDTO getSeries(@PathVariable("name") String name) {
        Optional<SeriesDTO> seriesDTOOptional = seriesRespository.querySeriesByName(name);
        if (seriesDTOOptional.isPresent()) {
            return seriesDTOOptional.get();
        } else {
            throw new SeriesNotFoundException();
        }
    }

    @DeleteMapping("/{name}")
    public void deleteSeries(@PathVariable("name") String name) {
        seriesRespository.deleteSeries(name);
    }


    @GetMapping("/{name}/images")
    public ResponseEntity<Resource> getSeriesImage(@PathVariable("name") String name) throws FileNotFoundException {
        Optional<SeriesDTO> seriesDTOOptional = seriesRespository.querySeriesByName(name);
        if (seriesDTOOptional.isPresent()) {
            File file = new File(seriesDTOOptional.get().getSeriesImage());
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("image/png"))
                    .body(resource);
        } else {
            throw new RolesNotFoundException();
        }
    }

    @GetMapping("/{name}/headerimage")
    public ResponseEntity<Resource> getSeriesHeaderImage(@PathVariable("name") String name) throws FileNotFoundException {
        Optional<SeriesDTO> seriesDTOOptional = seriesRespository.querySeriesByName(name);
        if (seriesDTOOptional.isPresent()) {
            File file = new File(seriesDTOOptional.get().getMatrixHeaderImage());
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("image/png"))
                    .body(resource);
        } else {
            throw new RolesNotFoundException();
        }
    }

    @GetMapping("/{name}/cellimage")
    public ResponseEntity<Resource> getSeriesCellImage(@PathVariable("name") String name) throws FileNotFoundException {
        Optional<SeriesDTO> seriesDTOOptional = seriesRespository.querySeriesByName(name);
        if (seriesDTOOptional.isPresent()) {
            File file = new File(seriesDTOOptional.get().getMatrixCellImage());
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("image/png"))
                    .body(resource);
        } else {
            throw new RolesNotFoundException();
        }
    }

    public String getSeriesFolder(String name) {
        File seriesBase = new File(imagePath + "/series/");
        if (!seriesBase.exists()) {
            seriesBase.mkdir();
        }
        File rolesFolder = new File(imagePath + "/series/" + name + "/");
        if (!rolesFolder.exists()) {
            rolesFolder.mkdir();
        }
        return imagePath + "/series/" + name + "/";
    }
}
