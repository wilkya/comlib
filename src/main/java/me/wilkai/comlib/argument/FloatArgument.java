package me.wilkai.comlib.argument;

import me.wilkai.comlib.Argument;
import me.wilkai.comlib.CommandContext;
import me.wilkai.comlib.exception.CommandException;
import me.wilkai.comlib.exception.CommandSyntaxException;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class FloatArgument extends ArgumentType<Float> {

    public Float parse(String input, CommandContext<?> context) throws CommandException {
        if (input.length() >= 50) {
            throw new CommandSyntaxException("Number too large!");
        }

        try {
            return Float.parseFloat(input);
        } catch (NumberFormatException e) {
            throw new CommandSyntaxException("Expected a decimal but instead got %s?", input);
        }
    }

    public OptionData suggest(Argument argument) {
        return new OptionData(OptionType.STRING, argument.name(), argument.summary(), argument.required());
    }
}
