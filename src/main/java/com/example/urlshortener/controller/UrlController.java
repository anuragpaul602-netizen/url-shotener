package com.example.urlshortener.controller;

import com.example.urlshortener.model.UrlMapping;
import com.example.urlshortener.service.UrlService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.net.URI;
import com.example.urlshortener.dto.UrlRequest;
import java.util.Optional;

@RestController
public class UrlController {

    private final UrlService service;

    public UrlController(UrlService service) {
        this.service = service;
    }

    @PostMapping("/shorten")
    public String shorten(@RequestBody UrlRequest request) {

        String code = service.shortenUrl(
                request.getUrl(),
                request.getCustomCode()
        );

        return "http://localhost:8080/" + code;
    }

    @GetMapping("/{code}")
    public ResponseEntity<Void> redirect(@PathVariable String code) {

        Optional<UrlMapping> url = service.getOriginalUrl(code);

        if (url.isPresent()) {

            URI uri = URI.create(url.get().getLongUrl());

            return ResponseEntity
                    .status(HttpStatus.FOUND)
                    .location(uri)
                    .build();
        }

        return ResponseEntity.notFound().build();
    }
}