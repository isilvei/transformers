package com.incode.demo.transformers.domain;


public interface Transformer <T,R> {

    R transform(T t);

    String transformerId();
}
