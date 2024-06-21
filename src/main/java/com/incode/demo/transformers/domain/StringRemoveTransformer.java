package com.incode.demo.transformers.domain;

import org.springframework.stereotype.Component;


@Component
public class StringRemoveTransformer extends AbstractStringTransformer{
    public StringRemoveTransformer() {
        super("StringTransformer", "string-remove-transformer");
    }

    @Override
    public String transform(String inputString) {
        String result = inputString;
        for(String regexParam: this.parameters()){
            result = result.replaceAll(regexParam,"");
        }
        return result;
    }
}
