package com.incode.demo.transformers.controller;

import com.incode.demo.transformers.domain.TransformRequest;
import com.incode.demo.transformers.domain.TransformResponse;
import com.incode.demo.transformers.service.TransformerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class TransformersController {

    private static final Logger logger = LoggerFactory.getLogger(TransformersController.class);

    private final TransformerService transformerService;

    public TransformersController(TransformerService transformerService) {
        this.transformerService = transformerService;
    }

    @PostMapping("/api/transform")
    public ResponseEntity<TransformResponse> transformString(@Valid @RequestBody TransformRequest transformRequest) {

        logger.debug("Transformer request: {}", transformRequest);
        if(Boolean.TRUE.equals(transformerService.existingTransformer(transformRequest.getElements()))) {
            TransformResponse transformResponse = transformerService.processTransformRequest(transformRequest);

            return ResponseEntity.of(Optional.ofNullable(transformResponse));
        }
        logger.debug("Transformer not found request: {}", transformRequest);
        TransformResponse defaultResponse = TransformResponse.builder()
                .elements(transformRequest.getElements())
                .build();

        return ResponseEntity.of(Optional.ofNullable(defaultResponse));

    }
}

