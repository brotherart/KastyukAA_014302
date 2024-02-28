package com.autodocservice.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
@Getter
public enum Role implements GrantedAuthority {
    ADMIN("Администратор"),
    HEAD("Начальник"),
    USER("Работник канцелярии"),
    WORKER("Сотрудник"),
    ;

    private final String name;

    @Override
    public String getAuthority() {
        return name();
    }
}

