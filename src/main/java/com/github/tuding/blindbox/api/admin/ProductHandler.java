package com.github.tuding.blindbox.api.admin;

import com.github.tuding.blindbox.api.admin.dto.ProductDTO;
import com.github.tuding.blindbox.api.admin.dto.SeriesDTO;
import com.github.tuding.blindbox.exception.ProductNotFoundException;
import com.github.tuding.blindbox.exception.RolesNotFoundException;
import com.github.tuding.blindbox.exception.SeriesNotFoundException;
import com.github.tuding.blindbox.infrastructure.repository.ProductRepository;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/product")
public class ProductHandler {

    @Autowired
    SeriesRespository seriesRespository;

    @Autowired
    ProductRepository productRepository;

    @Value("${app.imagePath}")
    private String imagePath;

    @PostMapping("/{seriesName}")
    public RedirectView handleForm(@PathVariable("seriesName") String seriesName,
                                   @RequestParam("name") String name,
                                   @RequestParam("isSpecial") boolean isSpecial,
                                   @RequestParam("stock") long stock,
                                   @RequestParam("probability") BigDecimal probability,
                                   @RequestParam("productImage") MultipartFile productImage,
                                   @RequestParam("postCardImage") MultipartFile postCardImage) throws IOException {
        UUID productID = UUID.randomUUID();
        log.info("Handle product creation as id {} series name {} product name {} isSpecial {} " +
                " stock {} probability {}", productID.toString(), seriesName, name, isSpecial, stock, probability);

        Optional<SeriesDTO> seriesDTOOptional = seriesRespository.querySeriesByName(seriesName);
        if (seriesDTOOptional.isPresent()) {
            File productImageFile = new File(getProductFolder(productID.toString()) + "image" + ".png");
            productImage.transferTo(productImageFile);
            File postCardImageFile = new File(getProductFolder(productID.toString()) + "postcard" + ".png");
            postCardImage.transferTo(postCardImageFile);

            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(productID.toString());
            productDTO.setSeriesID(seriesDTOOptional.get().getId());
            productDTO.setName(name);
            productDTO.setSpecial(isSpecial);
            productDTO.setStock(stock);
            productDTO.setProbability(probability);
            productDTO.setProductImage(productImageFile.getCanonicalPath());
            productDTO.setPostCardImage(postCardImageFile.getCanonicalPath());
            productRepository.createProduct(productDTO);
            return new RedirectView("/admin-ui/product/?series=" + seriesDTOOptional.get().getName());

        } else {
            throw new SeriesNotFoundException();
        }

    }

    @GetMapping("/series/{name}")
    public List<ProductDTO> getProductList(@PathVariable("name")String seriesName) {
        Optional<SeriesDTO> seriesDTOOptional = seriesRespository.querySeriesByName(seriesName);
        if (seriesDTOOptional.isPresent()) {
            return productRepository.getProductBySeries(seriesDTOOptional.get().getId());
        } else {
            return Collections.emptyList();
        }
    }

    @GetMapping("/{name}")
    public ProductDTO getSeries(@PathVariable("name") String name) {
        Optional<ProductDTO> productDTOOptional = productRepository.getProductByName(name);
        if (productDTOOptional.isPresent()) {
            return productDTOOptional.get();
        } else {
            throw new ProductNotFoundException();
        }
    }

    @GetMapping("/{name}/images")
    public ResponseEntity<Resource> getSeriesImage(@PathVariable("name") String name) throws FileNotFoundException {
        Optional<ProductDTO> productDTOOptional = productRepository.getProductByName(name);
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

    @GetMapping("/{name}/postcard")
    public ResponseEntity<Resource> getPostcardImage(@PathVariable("name") String name) throws FileNotFoundException {
        Optional<ProductDTO> productDTOOptional = productRepository.getProductByName(name);
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



    @DeleteMapping("/{name}")
    public void deleteProduct(@PathVariable("name") String name) {
        productRepository.deleteProduct(name);
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
