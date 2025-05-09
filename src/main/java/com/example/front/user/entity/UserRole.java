package com.example.front.user.entity;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("admin"),
    USER("user");

    private String value;

    UserRole(String value) {
        this.value = value;
    }

    public static UserRole fromValue(String value) {
        for (UserRole userRole : UserRole.values()) {
            if (userRole.getValue().equals(value)) {
                return userRole;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
