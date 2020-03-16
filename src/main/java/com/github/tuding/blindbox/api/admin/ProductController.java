package com.github.tuding.blindbox.api.admin;

import com.github.tuding.blindbox.api.admin.dto.ProductDTO;
import com.github.tuding.blindbox.api.admin.dto.SeriesDTO;
import com.github.tuding.blindbox.infrastructure.repository.ProductRepository;
import com.github.tuding.blindbox.infrastructure.repository.SeriesRespository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/admin-ui/product")
public class ProductController {

    @Autowired
    SeriesRespository seriesRespository;

    @Autowired
    ProductRepository productRepository;

    @GetMapping("/")
    public String seriesPage(Model model,
                             @RequestParam("seriesName") String seriesName) {
        Optional<SeriesDTO> seriesDTOOptional = seriesRespository.querySeriesByName(seriesName);
        List<ProductDTO> productDTOs = productRepository.getProductBySeries(seriesDTOOptional.get().getId());
        model.addAttribute("series", seriesDTOOptional.get());
        model.addAttribute("products", productDTOs);
        return "product";
    }
}
