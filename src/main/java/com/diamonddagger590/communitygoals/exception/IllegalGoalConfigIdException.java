package com.diamonddagger590.communitygoals.exception;

import org.jetbrains.annotations.NotNull;

public class IllegalGoalConfigIdException extends Exception {

    public IllegalGoalConfigIdException() {
        super();
    }

    public IllegalGoalConfigIdException(@NotNull String message) {
        super(message);
    }
}
