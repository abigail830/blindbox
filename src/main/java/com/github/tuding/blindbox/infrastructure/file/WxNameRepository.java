package com.github.tuding.blindbox.infrastructure.file;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Getter
@Repository
@Slf4j
public class WxNameRepository {

    private List<String> nameList;

    @PostConstruct
    void init() {
        try {
            final InputStream inputStream = getClass().getResourceAsStream("/wx_naming.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            nameList = br.lines().map(String::new).collect(Collectors.toList());
            br.close();
            log.info("wx_naming loaded with {} records. ", nameList.size());
        } catch (IOException e) {
            log.error("Fatal error met and fail to read wx_naming.csv {}", e);
            System.exit(1);
        }
    }

    public String getRandomWxName() {
        Random random = new Random();
        return nameList.get(random.nextInt(nameList.size()));
    }


}



