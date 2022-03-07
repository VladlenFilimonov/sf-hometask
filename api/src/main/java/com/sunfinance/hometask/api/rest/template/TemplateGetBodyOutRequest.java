package com.sunfinance.hometask.api.rest.template;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import java.util.Map;

@Value
@Builder
@Jacksonized
public class TemplateGetBodyOutRequest {

    @NotBlank
    String slug;

    Map<String, Object> variables;

}
