package com.incode.demo.transformers.domain;

import org.springframework.stereotype.Component;


@Component
public class StringReplaceTransformer extends AbstractStringTransformer{
    public StringReplaceTransformer() {
        super("StringTransformer", "string-replace-transformer");
    }

    @Override
    public String transform(String inputString) {

        String regex = (String) this.parameters().toArray()[0];
        String replaceStr = (String) this.parameters().toArray()[1];
        return inputString.replaceAll(regex,replaceStr);
    }
}
