package com.sunfinance.hometask.event.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicProperties {

    @NotBlank
    private String name;
    @Builder.Default
    private int partitions = 1;
    @Builder.Default
    private short replicationFactor = 1;
    private String replyTopicName;

}
