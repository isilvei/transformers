package com.incode.demo.transformers.domain;

import org.springframework.stereotype.Component;

import static com.incode.demo.transformers.domain.CyrillicToLatinMapping.CYRILLIC_TO_LATIN_MAP;
import static com.incode.demo.transformers.domain.GreekToLatinMapping.GREEK_TO_LATIN_MAP;


@Component
public class StringTransliterationTransformer extends AbstractStringTransformer{
    public StringTransliterationTransformer() {
        super("StringTransformer", "string-transliteration-transformer");
    }

    public static String convertToLatin(String input) {
        StringBuilder output = new StringBuilder();

        for (char ch : input.toCharArray()) {
            if (CYRILLIC_TO_LATIN_MAP.containsKey(ch)) {
                output.append(CYRILLIC_TO_LATIN_MAP.get(ch));
            } else if (GREEK_TO_LATIN_MAP.containsKey(ch)) {
                output.append(GREEK_TO_LATIN_MAP.get(ch));
            } else {
                output.append(ch);
            }
        }

        return output.toString();
    }


    @Override
    public String transform(String inputString) {
        return convertToLatin(inputString);
    }
}
