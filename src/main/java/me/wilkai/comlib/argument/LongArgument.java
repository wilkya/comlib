package me.wilkai.comlib.argument;

import me.wilkai.comlib.Argument;
import me.wilkai.comlib.CommandContext;
import me.wilkai.comlib.exception.CommandException;
import me.wilkai.comlib.exception.CommandSyntaxException;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class LongArgument extends ArgumentType<Long> {

    public Long parse(String input, CommandContext<?> context) throws CommandException {
        try {
            return Long.parseLong(input);
        } catch (NumberFormatException e) {
            throw new CommandSyntaxException("Expected a number but instead got %s?", input);
        }
    }

    public OptionData suggest(Argument argument) {
        return new OptionData(OptionType.INTEGER, argument.name(), argument.summary(), argument.required());
    }

}
