package com.incode.demo.transformers.service;

import com.incode.demo.transformers.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Validated
@Service
public class TransformerService {

    private static final Logger logger = LoggerFactory.getLogger(TransformerService.class);

    private final Collection<AbstractStringTransformer> transformers;

    public TransformerService(Collection<AbstractStringTransformer> transformers) {
        this.transformers = transformers;
    }

    public Boolean existingTransformer(Collection<Element> elements){
        Set<String> transformersIds = this.transformers.stream()
                .map(AbstractStringTransformer::transformerId)
                .collect(Collectors.toSet());
        return elements.stream()
                .flatMap(element -> element.getTransformers().stream())
                .anyMatch(transformerRule -> transformersIds.contains(transformerRule.getTransformerId()));
    }

    public TransformResponse processTransformRequest(TransformRequest request) {

        List<Element> elementResponseList = new ArrayList<>();

        for (Element element: request.getElements()){
            List<TransformerRule> transformerRuleList = new ArrayList<>();
            String result = element.getValue();
            for (TransformerRule transformerRule: element.getTransformers()) {
                String transformerId = transformerRule.getTransformerId();
                result = applyTransform(result, transformerRule);

                transformerRuleList.add(TransformerRule.builder()
                        .group(transformerRule.getGroup())
                        .transformedValue(result)
                        .parameters(transformerRule.getParameters())
                        .transformerId(transformerId)
                        .originalValue(element.getValue())
                        .build());
            }
            element.setTransformedValue(result);
            element.setTransformers(transformerRuleList);
            elementResponseList.add(element);
        }
        TransformResponse transformResponse = TransformResponse.builder()
                .elements(elementResponseList)
                .build();
        logger.debug("transformResponse is: {}", transformResponse);

        return transformResponse;
    }

    private String applyTransform(String value, TransformerRule transformerRule){
        AbstractStringTransformer transformer =  getStringTransformer(transformerRule.getTransformerId(),
                transformerRule.getGroup());

        if(transformer == null) return value;
        transformer.setParameters(transformerRule.getParameters());
        return transformer.transform(value);
    }


    public AbstractStringTransformer getStringTransformer(String transformerId, String group){
        return transformers
                .stream()
                .filter(transformer -> (transformer.transformerId().equals(transformerId)
                && transformer.group().equals(group)))
                .findFirst()
                .orElse(null);
    }
}
