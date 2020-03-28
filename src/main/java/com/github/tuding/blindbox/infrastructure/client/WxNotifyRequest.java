package com.github.tuding.blindbox.infrastructure.client;


import lombok.*;

import java.util.Map;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WxNotifyRequest {

    private String access_token;
    private String page;
    private String touser;
    private String template_id;
    private Map<String, TemplateData> data;

}
