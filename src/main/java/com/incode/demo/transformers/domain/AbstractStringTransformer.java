package com.incode.demo.transformers.domain;

import jakarta.validation.constraints.NotBlank;

import java.util.Collection;

public abstract class AbstractStringTransformer implements Transformer<String, String> {

    @NotBlank
    private final String group;
    @NotBlank
    private final String transformerId;

    private Collection<String> parameters;

    protected AbstractStringTransformer(String group, String transformerId) {
        this.group = group;
        this.transformerId = transformerId;
    }

    @Override
    public String transformerId() {
        return transformerId;
    }

    public @NotBlank String group() {
        return group;
    }


    public Collection<String> parameters() {
        return parameters;
    }

    public void setParameters( Collection<String> parameters) {
        this.parameters = parameters;
    }
}
