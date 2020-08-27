package com.github.tuding.blindbox.domain;

import com.github.tuding.blindbox.infrastructure.repository.BarrageRepository;
import com.github.tuding.blindbox.infrastructure.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BarrageService {

    public static final int TOTAL_BARRAGE_SIZE = 3;
    @Autowired
    BarrageRepository barrageRepository;

    @Autowired
    ProductRepository productRepository;

    private List<String> barrages = new ArrayList<>();

    public List<String> getBarrage() {
        List<String> latestThreeOrderBarrage = barrageRepository.getLatestThreeOrderBarrage();
        List<String> newBarrageList = new ArrayList<>(new HashSet<>(latestThreeOrderBarrage));
        barrages = newBarrageList.stream().filter(barrages::contains).collect(Collectors.toList());

        if (barrages.size() < TOTAL_BARRAGE_SIZE) {
            barrages.addAll(getDummyRandomBarrage(TOTAL_BARRAGE_SIZE - barrages.size()));
        }
        return barrages;
    }

    private List<String> getDummyRandomBarrage(Integer size) {
        final List<String> threeRandomProductName = productRepository.getThreeRandomProductName();
        if (threeRandomProductName.isEmpty()) {
            return Collections.emptyList();
        } else {
            return threeRandomProductName.stream().map(product -> {
                final String firstName = barrageRepository.getRandomWxName();
                return firstName + "成功抽中" + product;
            }).limit(size).collect(Collectors.toList());
        }
    }
}
