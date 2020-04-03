package com.github.tuding.blindbox.infrastructure.util;

import com.google.common.base.Strings;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class IpUtil {

    public String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (containIP(ip)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (containIP(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }

    private boolean containIP(String ip) {
        return !Strings.isNullOrEmpty(ip) && !"unKnown".equalsIgnoreCase(ip);
    }
}
