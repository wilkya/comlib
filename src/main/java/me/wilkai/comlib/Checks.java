package me.wilkai.comlib;

import me.wilkai.comlib.exception.CommandException;

/**
 * Contains basic checks to be used by Commands.
 */
public class Checks {

    /**
     * Checks to make sure an object is not null, throwing a CommandException if it is.
     * @param object The object whose nullness is to be checked.
     * @param message The message to print if the Check fails.
     * @param args The arguments to format into the message.
     * @throws CommandException If the object provided is, in fact, null.
     */
    public static void isNotNull(Object object, String message, Object... args) throws CommandException {
        if(object == null) {
            throw new CommandException(message, args);
        }
    }

    /**
     * Checks to make sure the provided expression is True, throwing a CommandException if it is not.
     * @param expression The expression that should be True.
     * @param message The message to print if the expression is not true.
     * @param args The arguments to format into the message.
     * @throws CommandException If the expression is false.
     */
    public static void isTrue(boolean expression, String message, Object... args) throws CommandException {
        if(!expression) {
            throw new CommandException(message, args);
        }
    }

    /**
     * Checks to make sure the provided expression is False, throwing a CommandException if it is True.
     * @param expression The expression that should be False.
     * @param message The message to print if the expression is not false.
     * @param args The arguments to format into the message.
     * @throws CommandException If the expression is true.
     */
    public static void isFalse(boolean expression, String message, Object... args) throws CommandException {
        if(expression) {
            throw new CommandException(message, args);
        }
    }

    /**
     * Checks to make sure the provided number is not 0, throwing a CommandException if it is.
     * @param number The number to check.
     * @param message THe message to print if the number is 0.
     * @param args The arguments to format into the message.
     * @throws CommandException If the provided number is 0.
     */
    public static void isNotZero(int number, String message, Object... args) throws CommandException {
        if(number == 0) {
            throw new CommandException(message, args);
        }
    }

}
