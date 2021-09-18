package me.wilkai.comlib.argument;

import me.wilkai.comlib.Argument;
import me.wilkai.comlib.CommandContext;
import me.wilkai.comlib.exception.CommandException;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class StringArgument extends ArgumentType<String> {

    public String parse(String input, CommandContext<?> context) throws CommandException {
        return input;
    }

    public OptionData suggest(Argument argument) {
        return new OptionData(OptionType.STRING, argument.name(), argument.summary(), argument.required());
    }

}
