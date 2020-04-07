package com.github.tuding.blindbox.domain;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@DBRider
class BarrageServiceTest {

    @Autowired
    BarrageService barrageService;

    @Test
    @DataSet("expect-data/save-product.yml")
    void getRandomBarrage() {

        final List<String> randomBarrage = barrageService.getRandomBarrage();
        System.out.println(randomBarrage);
//        assertEquals(3, randomBarrage.size());
    }
}