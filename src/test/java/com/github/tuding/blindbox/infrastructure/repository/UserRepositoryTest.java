package com.github.tuding.blindbox.infrastructure.repository;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.tuding.blindbox.domain.User;
import com.github.tuding.blindbox.infrastructure.util.Toggle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @DataSet("test-data/empty-user.yml")
    @ExpectedDataSet("expect-data/save-user-with-openId.yml")
    void saveUserWithOpenId() throws SQLException {
        Toggle.TEST_MODE.setStatus(true);
        userRepository.saveUserWithOpenId("openId");
    }

    @Test
    @DataSet("test-data/empty-user.yml")
    void queryAllUser() throws SQLException {
        //given
        Toggle.TEST_MODE.setStatus(true);
        userRepository.saveUserWithOpenId("oTA-N5rCXJmZsaDKxnB4vA1Tle8I");
        //when
        final Optional<User> result = userRepository.getUserByOpenId("oTA-N5rCXJmZsaDKxnB4vA1Tle8I");
        //then
        assertTrue(result.isPresent());
    }


}