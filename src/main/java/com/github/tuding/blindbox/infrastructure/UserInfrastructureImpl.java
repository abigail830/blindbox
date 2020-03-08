package com.github.tuding.blindbox.infrastructure;

import com.github.tuding.blindbox.domain.User;
import com.github.tuding.blindbox.domain.UserInfrastructure;
import com.github.tuding.blindbox.exception.BizException;
import com.github.tuding.blindbox.exception.ErrorCode;
import com.github.tuding.blindbox.infrastructure.client.WxClient;
import com.github.tuding.blindbox.infrastructure.repository.UserRepository;
import com.github.tuding.blindbox.infrastructure.security.JWTTokenHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class UserInfrastructureImpl implements UserInfrastructure {

    @Autowired
    WxClient wxClient;

    @Autowired
    JWTTokenHandler jwtTokenHandler;

    @Autowired
    UserRepository userRepository;

    @Override
    public Optional<String> getOpenId(String code) {
        return wxClient.getOpenId(code);
    }

    @Override
    public void saveUserWithOpenId(String openId) {
        userRepository.saveUserWithOpenId(openId);
    }

    @Override
    public String generateToken(String openId) {
        String token = jwtTokenHandler.generateWxToken(openId);
        log.info("token for openId[{}] is {}", openId, token);
        return token;
    }

    @Override
    public Optional<User> getUserByOpenId(String openId) {
        return userRepository.getUserByOpenId(openId);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.getAllUser();
    }

    @Override
    public User decrypt(String skey, String encryptedData, String iv) {
        return wxClient.decrypt(skey, encryptedData, iv);
    }

    @Override
    public Optional<User> updateUser(User decryptUser) {
        final int updatedRow = userRepository.updateWxDetailInfo(decryptUser);
        if (updatedRow == 1) {
            log.info("User updated for {}", decryptUser);
            return userRepository.getUserByOpenId(decryptUser.getOpenId());
        } else {
            throw new BizException(ErrorCode.FAIL_TO_UPDATE_USER);
        }
    }

    @Override
    public String getOpenIdFromToken(String token) {
        return jwtTokenHandler.verifyWxToken(token).getSubject();
    }
}
