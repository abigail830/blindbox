package com.github.tuding.blindbox.domain;

import com.github.tuding.blindbox.exception.BizException;
import com.github.tuding.blindbox.exception.ErrorCode;
import com.github.tuding.blindbox.infrastructure.client.WxClient;
import com.github.tuding.blindbox.infrastructure.repository.UserRepository;
import com.github.tuding.blindbox.infrastructure.security.Jwt;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WxClient wxClient;

    @Autowired
    Jwt jwt;

    @Transactional
    public String login(String code) {
        final User user = wxClient.getUerWithOpenIdAndSKey(code)
                .orElseThrow(() -> new BizException(ErrorCode.FAIL_TO_GET_OPENID));

        if (Strings.isNullOrEmpty(user.getOpenId())) {
            throw new BizException(ErrorCode.FAIL_TO_GET_OPENID);
        }

        final Optional<User> userByOpenId = userRepository.getUserByOpenId(user.getOpenId());
        if (!userByOpenId.isPresent()) {
            user.adjustBonusAndLastLoginDate();
            userRepository.addUserWithOpenIdWithBonus(user);
        } else {
            final User existUser = userByOpenId.get();
            existUser.adjustBonusAndLastLoginDate();
            userRepository.updateLastLoginDateAndBonus(existUser);
        }

        return jwt.generateWxToken(user);
    }

    public User getUserByToken(String token) {
        String openId = jwt.getOpenIdFromToken(token);
        log.info("Going to query user with openId: {}", openId);
        return userRepository.getUserByOpenId(openId)
                .orElseThrow(() -> new BizException(ErrorCode.WX_USER_NOT_FOUND));
    }

    public void wxAuth(String token, String encryptedData, String iv) {
        String sKey = jwt.getSKeyFromToken(token);
        final User decryptUser = wxClient.decrypt(sKey, encryptedData, iv);
        log.info("decryptUserInfo: {}", decryptUser);

        int updatedRow = userRepository.updateWxDetailInfo(decryptUser);
        if (updatedRow == 0) {
            throw new BizException(ErrorCode.FAIL_TO_UPDATE_USER_INFO);
        } else {
            log.info("User updated for {}", decryptUser);
        }

    }
}
