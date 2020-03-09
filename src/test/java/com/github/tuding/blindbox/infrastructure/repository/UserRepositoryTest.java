package com.github.tuding.blindbox.infrastructure.repository;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.github.tuding.blindbox.domain.User;
import com.github.tuding.blindbox.infrastructure.util.Toggle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DBRider
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @DataSet("test-data/empty-user.yml")
    @ExpectedDataSet("expect-data/save-user-with-openId.yml")
    void saveUserWithOpenId() throws SQLException {
        Toggle.TEST_MODE.setStatus(true);
        userRepository.saveUserWithOpenId(new User("openId", "sessionKey"));
    }

    @Test
    @DataSet("test-data/user-service-wxauth.yml")
    void queryAllUser() throws SQLException {
        //when
        final Optional<User> result = userRepository.getUserByOpenId("oGZUI0egBJY1zhBYw2KhdUfwVJJE");
        //then
        assertTrue(result.isPresent());
    }

    @Test
    @DataSet("test-data/user-service-wxauth.yml")
    void updateWxInfo() {
        final Optional<User> user = userRepository.getUserByOpenId("oGZUI0egBJY1zhBYw2KhdUfwVJJE");
        assertTrue(user.isPresent());
    }


}