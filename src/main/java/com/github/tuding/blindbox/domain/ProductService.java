package com.github.tuding.blindbox.domain;

import com.github.tuding.blindbox.api.wx.wxDto.DrawDTO;
import com.github.tuding.blindbox.infrastructure.repository.ProductRepository;
import com.github.tuding.blindbox.infrastructure.repository.RolesRepository;
import com.github.tuding.blindbox.infrastructure.repository.SeriesRespository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class ProductService {

    @Autowired
    RolesRepository rolesRepository;

    @Autowired
    SeriesRespository seriesRespository;

    @Autowired
    ProductRepository productRepository;
    
    @Autowired

    public List<Role> getRoles() {
        return rolesRepository.queryRoles();
    }


    public List<Series> getSeriesList(String roleId) {
        return seriesRespository.queryByRoleID(roleId);
    }

    public Optional<Series> getSeries(String seriesId) {
        return seriesRespository.querySeriesByID(seriesId);
    }

}
