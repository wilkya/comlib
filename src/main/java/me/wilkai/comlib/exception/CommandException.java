package me.wilkai.comlib.exception;

public class CommandException extends Exception {

    public CommandException(String message, Object... args) {
        super(String.format(message, args));
    }
}