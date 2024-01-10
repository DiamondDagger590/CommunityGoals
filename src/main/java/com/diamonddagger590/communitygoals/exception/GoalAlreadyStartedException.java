package com.diamonddagger590.communitygoals.exception;

import org.jetbrains.annotations.NotNull;

public class GoalAlreadyStartedException extends RuntimeException {

    public GoalAlreadyStartedException() {
        super();
    }

    public GoalAlreadyStartedException(@NotNull String message) {
        super(message);
    }
}
