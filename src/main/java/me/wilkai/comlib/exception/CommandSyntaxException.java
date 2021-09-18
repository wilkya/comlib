package me.wilkai.comlib.exception;

/**
 * Exception that is thrown whenever an argument of a command is invalid.
 */
public class CommandSyntaxException extends CommandException {
    public CommandSyntaxException(String message, Object... args) {
        super(String.format(message, args));
    }
}