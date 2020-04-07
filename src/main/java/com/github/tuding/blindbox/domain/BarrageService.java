package com.github.tuding.blindbox.domain;

import com.github.tuding.blindbox.infrastructure.file.WxNameRepository;
import com.github.tuding.blindbox.infrastructure.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BarrageService {

    @Autowired
    WxNameRepository wxNameRepository;

    @Autowired
    ProductRepository productRepository;

    public List<String> getRandomBarrage() {
        final List<String> threeRandomProductName = productRepository.getThreeRandomProductName();
        return threeRandomProductName.stream().map(product -> {
            final String firstName = wxNameRepository.getRandomWxName();
            return firstName + "成功抽中" + product;
        }).collect(Collectors.toList());
    }
}
