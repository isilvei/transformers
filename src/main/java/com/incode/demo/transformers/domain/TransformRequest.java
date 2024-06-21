package com.incode.demo.transformers.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Collection;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@Setter(AccessLevel.NONE)
@AllArgsConstructor
@NoArgsConstructor
public class TransformRequest {



    @NotEmpty
    private Collection<Element> elements;

}