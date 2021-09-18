package me.wilkai.comlib.argument;

import me.wilkai.comlib.Argument;
import me.wilkai.comlib.CommandContext;
import me.wilkai.comlib.exception.CommandException;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public abstract class ArgumentType<T> {

    public abstract T parse(String input, CommandContext<?> context) throws CommandException;

    public abstract OptionData suggest(Argument argument);

}