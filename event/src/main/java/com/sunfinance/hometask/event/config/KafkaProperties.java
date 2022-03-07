package com.sunfinance.hometask.event.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KafkaProperties {

    @NotBlank
    private String bootstrapAddresses;
    @Valid
    @Builder.Default
    private Map<String, TopicProperties> topics = new HashMap<>();
    @Builder.Default
    private String groupId = "default";

}
