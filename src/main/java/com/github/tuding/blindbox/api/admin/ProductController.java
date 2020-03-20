package com.github.tuding.blindbox.api.admin;

import com.github.tuding.blindbox.api.admin.dto.ProductDTO;
import com.github.tuding.blindbox.api.admin.dto.SeriesDTO;
import com.github.tuding.blindbox.domain.ImageCategory;
import com.github.tuding.blindbox.domain.Product;
import com.github.tuding.blindbox.domain.Series;
import com.github.tuding.blindbox.exception.RolesNotFoundException;
import com.github.tuding.blindbox.exception.SeriesNotFoundException;
import com.github.tuding.blindbox.infrastructure.file.ImageRepository;
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
import org.springframework.web.servlet.view.RedirectView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/admin-ui/product")
public class ProductController {

    @Autowired
    SeriesRespository seriesRespository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ImageRepository imageRepository;

    @Value("${app.imagePath}")
    private String imagePath;

    @GetMapping("/")
    public String seriesPage(Model model,
                             @RequestParam("seriesId") String seriesID) {
        Optional<Series> seriesOptional = seriesRespository.querySeriesByID(seriesID);
        List<Product> products = productRepository.getProductBySeries(seriesOptional.get().getId());
        model.addAttribute("series", new SeriesDTO(seriesOptional.get()));
        model.addAttribute("products", products.stream().map(ProductDTO::new).collect(Collectors.toList()));
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

        final Optional<Product> productOptional = productRepository.getProductByID(id);
        productOptional.ifPresent(product -> {
                    model.addAttribute("seriesId", product.getSeriesID());
                    model.addAttribute("product", new ProductDTO(product));
                    log.info("Edit ProductDTO [{}]", product);
                }
        );
        return "productform";
    }


    @PostMapping("/form/{seriesId}")
    public RedirectView handleForm(@PathVariable("seriesId") String seriesID,
                                   @ModelAttribute("productForm") ProductDTO productDTO,
                                   Model model) throws IOException {
        Optional<Series> seriesOptional = seriesRespository.querySeriesByID(seriesID);
        if (seriesOptional.isPresent()) {
            if (StringUtils.isNotBlank(productDTO.getId())) {
                log.info("handle product update as {} id {}", productDTO, seriesID);

                if (productDTO.getProductImageFile().getSize() > 0) {
                    String image = imageRepository.saveImage(productDTO.getId(), ImageCategory.PRODUCT, productDTO.getProductImageFile());
                    productDTO.setProductImage(image);
                }

                if (productDTO.getPostCardImageFile().getSize() > 0){
                    String image = imageRepository.saveImage(productDTO.getId() + "postcard", ImageCategory.PRODUCT, productDTO.getPostCardImageFile());
                    productDTO.setPostCardImage(image);
                }
                productRepository.updateProduct(productDTO.toDomainObject());
                return new RedirectView("/admin-ui/product/?seriesId=" + seriesOptional.get().getId());
            } else {
                UUID productID = UUID.randomUUID();
                log.info("handle product creation as {} id {}", productDTO, productID.toString());

                String image = imageRepository.saveImage(productID.toString(), ImageCategory.PRODUCT, productDTO.getProductImageFile());
                productDTO.setProductImage(image);
                image = imageRepository.saveImage(productID.toString() + "postcard", ImageCategory.PRODUCT, productDTO.getPostCardImageFile());
                productDTO.setPostCardImage(image);

                productDTO.setId(productID.toString());
                productDTO.setSeriesID(seriesID);
                productRepository.createProduct(productDTO.toDomainObject());
                return new RedirectView("/admin-ui/product/?seriesId=" + seriesOptional.get().getId());
            }
        } else {
            throw new SeriesNotFoundException();
        }

    }

    @GetMapping("/series/{id}")
    public List<ProductDTO> getProductList(@PathVariable("id")String seriesID) {
        Optional<Series> seriesOptional = seriesRespository.querySeriesByID(seriesID);
        if (seriesOptional.isPresent()) {
            List<Product> productBySeries = productRepository.getProductBySeries(seriesOptional.get().getId());
            return productBySeries.stream().map(ProductDTO::new).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @GetMapping("/{id}/images")
    public ResponseEntity<Resource> getSeriesImage(@PathVariable("id") String id) throws FileNotFoundException {
        Optional<Product> productOptional = productRepository.getProductByID(id);
        if (productOptional.isPresent()) {
            File file = new File(productOptional.get().getProductImage());
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
        Optional<Product> productOptional = productRepository.getProductByID(id);
        if (productOptional.isPresent()) {
            File file = new File(productOptional.get().getPostCardImage());
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


}
