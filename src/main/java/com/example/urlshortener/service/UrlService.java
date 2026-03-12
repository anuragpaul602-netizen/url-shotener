package com.example.urlshortener.service;

import com.example.urlshortener.model.UrlMapping;
import com.example.urlshortener.repository.UrlRepository;
import com.example.urlshortener.util.Base62Encoder;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Optional;

@Service
public class UrlService {

    private final UrlRepository repository;
    private final StringRedisTemplate redisTemplate;

    public UrlService(UrlRepository repository, StringRedisTemplate redisTemplate) {
        this.repository = repository;
        this.redisTemplate = redisTemplate;
    }

    public String shortenUrl(String longUrl, String customCode) {

        UrlMapping url = new UrlMapping();
        url.setLongUrl(longUrl);

        // If user provides custom code
        if (customCode != null && !customCode.isEmpty()) {
            url.setShortCode(customCode);
            repository.save(url);
            return customCode;
        }

        // Otherwise generate code automatically
        repository.save(url);

        String shortCode = Base62Encoder.encode(url.getId());
        url.setShortCode(shortCode);

        repository.save(url);

        return shortCode;
    }

    public Optional<UrlMapping> getOriginalUrl(String code) {

        String cachedUrl = redisTemplate.opsForValue().get(code);

        if (cachedUrl != null) {
            UrlMapping mapping = new UrlMapping();
            mapping.setLongUrl(cachedUrl);
            return Optional.of(mapping);
        }

        Optional<UrlMapping> url = repository.findByShortCode(code);

        if (url.isPresent()) {

            UrlMapping mapping = url.get();

            redisTemplate.opsForValue().set(code, mapping.getLongUrl());

            mapping.setClicks(mapping.getClicks() + 1);
            repository.save(mapping);
        }

        return url;
    }
}