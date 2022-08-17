package org.cloud.properties;

import cn.hutool.core.codec.Base64;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "jwt")
@Data
public class JWTProperties {

    private String key;
    private byte[] keyBytes;


    public void setKey(String key) {
        this.key = key;
        this.keyBytes = Base64.encode(key).getBytes();
    }
}
