package com.github.tuding.blindbox.infrastructure.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RetryUtil {
    private static final Logger log = LoggerFactory.getLogger(RetryUtil.class);
    public static interface ReturnableRetry<T> {
        public T retry() throws Exception;
    }

    public static <T> T retryOnTimes(ReturnableRetry<T> retry, int times, long internalInMillis) {
        int counter = 0;
        while (true) {
            try {
                return retry.retry();
            } catch (Exception ex) {
                counter ++;
                if (counter < times) {
                    log.warn("Retry on exception. ", ex);
                    if (internalInMillis > 0) {
                        try {
                            Thread.sleep(internalInMillis);
                        } catch (Exception e) {
                            log.warn("Failed to wait. ", e);
                        }
                    }
                } else {
                    log.error("Retry completed on exception. ", ex);
                    throw new RuntimeException(ex);
                }
            }
        }
    }
}
