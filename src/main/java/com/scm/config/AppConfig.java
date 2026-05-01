package com.scm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Configuration
public class AppConfig {

    @Value("${cloudinary.cloud.name:dummy}")
    private String cloudName;

    @Value("${cloudinary.api.key:dummy}")
    private String apiKey;

    @Value("${cloudinary.api.secret:dummy}")
    private String apiSecret;

    @Bean
    public Cloudinary cloudinary() {

        // 🔥 IMPORTANT FIX
        if ("dummy".equals(cloudName) || "dummy".equals(apiKey) || "dummy".equals(apiSecret)) {
            System.out.println("⚠️ Cloudinary not configured. Skipping...");
            return null;   // ❗ ye hi crash fix karega
        }

        return new Cloudinary(
                ObjectUtils.asMap(
                        "cloud_name", cloudName,
                        "api_key", apiKey,
                        "api_secret", apiSecret)
        );
    }
}