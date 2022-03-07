package com.sunfinance.hometask.verification.template.controller;

import com.sunfinance.hometask.api.rest.template.SlugConstants;
import com.sunfinance.hometask.api.rest.template.TemplateGetBodyOutRequest;
import com.sunfinance.hometask.verification.template.service.TemplateService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.concurrent.CompletableFuture;

@RestController
@AllArgsConstructor
public class TemplateRenderController {

    private final TemplateService templateService;

    @PostMapping("/templates/render")
    public CompletableFuture<ResponseEntity<String>> renderTemplate(@Valid @NotNull @RequestBody TemplateGetBodyOutRequest request) {
        return CompletableFuture.supplyAsync(() -> templateService.render(request))
                                .thenApply(text -> buildResponse(text, request));
    }

    private ResponseEntity<String> buildResponse(String text,
                                                 TemplateGetBodyOutRequest request) {
        var slug = SlugConstants.getSlug(request.getSlug());
        switch (slug) {
            case EMAIL:
                return ResponseEntity.ok()
                                     .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE, "charset=UTF-8")
                                     .body(text);
            case MOBILE:
                return ResponseEntity.ok()
                                     .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE, "charset=UTF-8")
                                     .body(text);
        }
        return ResponseEntity.badRequest().build();
    }
}
