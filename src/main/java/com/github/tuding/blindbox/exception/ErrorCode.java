package com.github.tuding.blindbox.exception;

public enum ErrorCode {

    FAIL_TO_GET_OPENID,

    INVALID_AES_KEY,
    INVALID_IV,
    ILLEGAL_BUFFER,
    FAIL_TO_DECRYPT,

    FAIL_TO_UPDATE_USER_INFO,
    WX_USER_NOT_FOUND,
    FAIL_TO_MODIFY_SHIPPING_ADDRESS,

    FAIL_TO_STORE_FILE,

    INVALID_ACTIVITY_ID,
    SHOULD_NOT_REG_NOTIFY_FOR_ACTIVITY_ALREADY_PASSED,

}
