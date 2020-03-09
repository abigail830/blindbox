package com.github.tuding.blindbox.infrastructure.repository;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.github.tuding.blindbox.infrastructure.security.DefaultEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@SpringBootTest
@DBRider
class AdminLoginTest {

    @Autowired
    AdminUserRepository adminUserRepository;


    @Test
    @DataSet("test-data/admin-user.yml")
    void testCanLoggin() {
        assertThat(adminUserRepository.isValidAdmin("admin", "adminpwd", new DefaultEncryptor("unittest")),
                is(true));
        assertThat(adminUserRepository.isValidAdmin("admin", "adminpwd2", new DefaultEncryptor("unittest")),
                is(false));
        assertThat(adminUserRepository.isValidAdmin("admin1", "adminpwd", new DefaultEncryptor("unittest")),
                is(false));
    }



}