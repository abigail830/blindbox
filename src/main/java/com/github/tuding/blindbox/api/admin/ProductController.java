package com.github.tuding.blindbox.api.admin;

import com.github.tuding.blindbox.api.admin.dto.ProductDTO;
import com.github.tuding.blindbox.api.admin.dto.RoleDTO;
import com.github.tuding.blindbox.api.admin.dto.SeriesDTO;
import com.github.tuding.blindbox.exception.RolesNotFoundException;
import com.github.tuding.blindbox.exception.SeriesNotFoundException;
import com.github.tuding.blindbox.infrastructure.repository.ProductRepository;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/admin-ui/product")
public class ProductController {

    @Autowired
    SeriesRespository seriesRespository;

    @Autowired
    ProductRepository productRepository;

    @Value("${app.imagePath}")
    private String imagePath;

    @GetMapping("/")
    public String seriesPage(Model model,
                             @RequestParam("seriesId") String seriesID) {
        Optional<SeriesDTO> seriesDTOOptional = seriesRespository.querySeriesByID(seriesID);
        List<ProductDTO> productDTOs = productRepository.getProductBySeries(seriesDTOOptional.get().getId());
        model.addAttribute("series", seriesDTOOptional.get());
        model.addAttribute("products", productDTOs);
        return "product";
    }

    @GetMapping("/productform")
    public String createFrom(Model model,
                             @RequestParam("seriesId") String seriesID) {
        model.addAttribute("seriesId", seriesID);
        model.addAttribute("product", new ProductDTO());

        return "productform";
    }

    @GetMapping("/productform/{id}")
    public String editFrom(Model model, @PathVariable String id) {

        final Optional<ProductDTO> productDTOOptional = productRepository.getProductByID(id);
        productDTOOptional.ifPresent(roleDTO -> {
                    model.addAttribute("seriesId", productDTOOptional.get().getSeriesID());
                    model.addAttribute("product", productDTOOptional.get());
                    log.info("Edit ProductDTO [{}]", productDTOOptional.get());
                }
        );
        return "productform";
    }


    @PostMapping("/form/{seriesId}")
    public RedirectView handleForm(@PathVariable("seriesId") String seriesID,
                                   @ModelAttribute("productForm") ProductDTO productDTO,
                                   Model model) throws IOException {
        Optional<SeriesDTO> seriesDTOOptional = seriesRespository.querySeriesByID(seriesID);
        if (seriesDTOOptional.isPresent()) {
            if (StringUtils.isNotBlank(productDTO.getId())) {
                log.info("handle product update as {} id {}", productDTO, seriesID.toString());
                File productImageFile = new File(getProductFolder(productDTO.getId()) + "image" + ".png");
                if (productDTO.getProductImageFile().getSize() > 0) {
                    productDTO.getProductImageFile().transferTo(productImageFile);
                }
                File postCardImageFile = new File(getProductFolder(productDTO.getId()) + "postcard" + ".png");
                if (productDTO.getPostCardImageFile().getSize() > 0){
                    productDTO.getPostCardImageFile().transferTo(postCardImageFile);
                }


                productDTO.setProductImage(productImageFile.getCanonicalPath());
                productDTO.setPostCardImage(postCardImageFile.getCanonicalPath());
                productRepository.updateProduct(productDTO);
                return new RedirectView("/admin-ui/product/?seriesId=" + seriesDTOOptional.get().getId());
            } else {
                UUID productID = UUID.randomUUID();
                log.info("handle product creation as {} id {}", productDTO, seriesID.toString());

                File productImageFile = new File(getProductFolder(productID.toString()) + "image" + ".png");
                productDTO.getProductImageFile().transferTo(productImageFile);
                File postCardImageFile = new File(getProductFolder(productID.toString()) + "postcard" + ".png");
                productDTO.getPostCardImageFile().transferTo(postCardImageFile);


                productDTO.setId(productID.toString());
                productDTO.setSeriesID(seriesID);
                productDTO.setProductImage(productImageFile.getCanonicalPath());
                productDTO.setPostCardImage(postCardImageFile.getCanonicalPath());
                productRepository.createProduct(productDTO);
                return new RedirectView("/admin-ui/product/?seriesId=" + seriesDTOOptional.get().getId());
            }
        } else {
            throw new SeriesNotFoundException();
        }

    }

    @GetMapping("/series/{id}")
    public List<ProductDTO> getProductList(@PathVariable("id")String seriesID) {
        Optional<SeriesDTO> seriesDTOOptional = seriesRespository.querySeriesByID(seriesID);
        if (seriesDTOOptional.isPresent()) {
            return productRepository.getProductBySeries(seriesDTOOptional.get().getId());
        } else {
            return Collections.emptyList();
        }
    }

    @GetMapping("/{id}/images")
    public ResponseEntity<Resource> getSeriesImage(@PathVariable("id") String id) throws FileNotFoundException {
        Optional<ProductDTO> productDTOOptional = productRepository.getProductByID(id);
        if (productDTOOptional.isPresent()) {
            File file = new File(productDTOOptional.get().getProductImage());
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("image/png"))
                    .body(resource);
        } else {
            throw new RolesNotFoundException();
        }
    }

    @GetMapping("/{id}/postcard")
    public ResponseEntity<Resource> getPostcardImage(@PathVariable("id") String id) throws FileNotFoundException {
        Optional<ProductDTO> productDTOOptional = productRepository.getProductByID(id);
        if (productDTOOptional.isPresent()) {
            File file = new File(productDTOOptional.get().getPostCardImage());
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("image/png"))
                    .body(resource);
        } else {
            throw new RolesNotFoundException();
        }
    }



    @DeleteMapping("/{id}")
    public @ResponseBody
    void deleteProduct(@PathVariable("id") String id) {
        productRepository.deleteProduct(id);
    }


    public String getProductFolder(String name) {
        File seriesBase = new File(imagePath + "/product/");
        if (!seriesBase.exists()) {
            seriesBase.mkdir();
        }
        File rolesFolder = new File(imagePath + "/product/" + name + "/");
        if (!rolesFolder.exists()) {
            rolesFolder.mkdir();
        }
        return imagePath + "/product/" + name + "/";
    }
}
