package com.github.tuding.blindbox.configuration;

import com.github.tuding.blindbox.infrastructure.security.DefaultEncryptor;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EncryptorConfig {

    @Bean(name = "jasyptStringEncryptor")
    public StringEncryptor stringEncryptor() {
        return new DefaultEncryptor();
    }
}
