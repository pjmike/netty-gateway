package com.pjmike.handler.predicate;

import lombok.Data;


/**
 * @description: 断言定义
 * @author: pjmike
 * @create: 2019/12/08
 */
@Data
public class PredicateDefinition {

    private String value;

    public PredicateDefinition() {
    }

    public PredicateDefinition(String text) {
        this.value = text;
    }
}
