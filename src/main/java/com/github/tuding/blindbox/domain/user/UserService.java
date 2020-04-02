package com.github.tuding.blindbox.domain.user;

import com.github.tuding.blindbox.exception.BizException;
import com.github.tuding.blindbox.exception.ErrorCode;
import com.github.tuding.blindbox.infrastructure.client.WxClient;
import com.github.tuding.blindbox.infrastructure.repository.UserRepository;
import com.github.tuding.blindbox.infrastructure.security.Jwt;
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

        final Optional<User> userByOpenId = userRepository.getUserByOpenId(user.getOpenId());
        if (!userByOpenId.isPresent()) {
            user.updateLoginBonus();
            userRepository.addUserWithOpenIdWithBonus(user);
        } else {
            final User existUser = userByOpenId.get();
            existUser.updateLoginBonus();
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

    public Integer consumeBonusForCoupon(String token, Integer consumeBonus) {
        final String openId = jwt.getOpenIdFromToken(token);
        final User user = userRepository.getUserByOpenId(openId)
                .orElseThrow(() -> new BizException(ErrorCode.WX_USER_NOT_FOUND));

        Integer remainBonus = user.getBonus() - consumeBonus;
        if (remainBonus < 0) {
            throw new BizException(ErrorCode.BONUS_NOT_ENOUGH);
        } else {
            userRepository.updateBonus(openId, remainBonus);
            return remainBonus;
        }
    }

    public void shareActivity(String token, String activityId) {
        final String openId = jwt.getOpenIdFromToken(token);
        log.info("User[{}] share activity {}", openId, activityId);

        final User user = userRepository.getUserByOpenId(openId)
                .orElseThrow(() -> new BizException(ErrorCode.WX_USER_NOT_FOUND));
        user.shareActivity();
        userRepository.updateLastShareActivityBonus(user);
    }

    public void shareCollection(String token, String seriesId) {
        final String openId = jwt.getOpenIdFromToken(token);
        log.info("User[{}] share collection for series {}", openId, seriesId);

        final User user = userRepository.getUserByOpenId(openId)
                .orElseThrow(() -> new BizException(ErrorCode.WX_USER_NOT_FOUND));
        user.shareCollection();
        userRepository.updateLastShareCollectionBonus(user);
    }

}
