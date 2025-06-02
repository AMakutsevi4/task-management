package ru.management.enums;

import lombok.Getter;

@Getter
public enum TaskPriority {
    USUAL(1),
    IMPORTANT(5),
    URGENT(10);
    private final int priority;

    TaskPriority(int priority) {
        this.priority = priority;
    }
}