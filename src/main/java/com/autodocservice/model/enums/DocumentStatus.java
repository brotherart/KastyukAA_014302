package com.autodocservice.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DocumentStatus {
    SIGNED("Просмотрено"),
    NOT_SIGNED("Не просмотрено"),
    ;

    private final String name;
}

