package me.wilkai.comlib;

import me.wilkai.comlib.exception.CommandException;

/**
 * <br> Base class for all Commands.
 * <br>
 * <br> This class will be automatically instantiated.
 * <br>
 * <br> Global Data stored by this command must be static as if the same command is executed the data contained within
 * will be reset.
 * @param <T> The type of context in which this command was executed.
 */
public abstract class Command<T extends CommandContext<?>> {

    /**
     * <br> The context in which this message was sent.
     * <br> This includes the User in question.
     * <br>
     * <br> This may be a Message Context, a Slash Context or both.
     */
    public T context;

    /**
     * Method which is called when a command is invoked.
     * @throws CommandException If an intentional exception occurs at any point.
     */
    public abstract void onCommand() throws CommandException;

}