package com.github.tuding.blindbox.api.admin;

import com.github.tuding.blindbox.api.admin.dto.ProductDTO;
import com.github.tuding.blindbox.api.admin.dto.SeriesDTO;
import com.github.tuding.blindbox.domain.ImageCategory;
import com.github.tuding.blindbox.domain.product.Product;
import com.github.tuding.blindbox.domain.product.Series;
import com.github.tuding.blindbox.exception.SeriesNotFoundException;
import com.github.tuding.blindbox.infrastructure.file.ImageRepository;
import com.github.tuding.blindbox.infrastructure.repository.ProductRepository;
import com.github.tuding.blindbox.infrastructure.repository.SeriesRespository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

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
        model.addAttribute("roleId", seriesOptional.get().getRoleId());
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
                } else {
                    productDTO.setProductImage(imageRepository.getPath(productDTO.getId(), ImageCategory.PRODUCT));
                }


                if (productDTO.getProductGrayImageFile().getSize() > 0) {
                    String image = imageRepository.saveImage(productDTO.getId() + "gray", ImageCategory.PRODUCT, productDTO.getProductGrayImageFile());
                    productDTO.setProductGrayImage(image);
                } else {
                    productDTO.setProductGrayImage(imageRepository.getPath(productDTO.getId() + "gray", ImageCategory.PRODUCT));
                }
                Product product = productDTO.toDomainObject();
                product.setVersion(product.getVersion() + 1);
                productRepository.updateProduct(product);
                return new RedirectView("/admin-ui/product/?seriesId=" + seriesOptional.get().getId());
            } else {
                UUID productID = UUID.randomUUID();
                log.info("handle product creation as {} id {}", productDTO, productID.toString());

                String image = imageRepository.saveImage(productID.toString(), ImageCategory.PRODUCT, productDTO.getProductImageFile());
                productDTO.setProductImage(image);

                image = imageRepository.saveImage(productID.toString() + "gray", ImageCategory.PRODUCT, productDTO.getProductGrayImageFile());
                productDTO.setProductGrayImage(image);

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



    @DeleteMapping("/{id}")
    public @ResponseBody
    void deleteProduct(@PathVariable("id") String id) {
        productRepository.deleteProduct(id);
    }


}
