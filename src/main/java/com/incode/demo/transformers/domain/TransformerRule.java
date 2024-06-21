package com.incode.demo.transformers.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.util.Collection;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@Setter(AccessLevel.NONE)
public class TransformerRule{

    @NotBlank
    private String group;

    @NotBlank
    private String transformerId;

    private Collection<String> parameters;

    private String originalValue;

    private String transformedValue;

}
