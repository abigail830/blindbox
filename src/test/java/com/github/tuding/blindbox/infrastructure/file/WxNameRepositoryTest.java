package com.github.tuding.blindbox.infrastructure.file;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WxNameRepositoryTest {

    @Test
    void init() {
        final WxNameRepository wxNameRepository = new WxNameRepository();
        wxNameRepository.init();
        System.out.println(wxNameRepository.getNameList());
        assertEquals(70, wxNameRepository.getNameList().size());
        assertEquals(70, wxNameRepository.getNameList().size());
    }
}