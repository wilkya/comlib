package me.wilkai.comlib.argument;

import me.wilkai.comlib.Argument;
import me.wilkai.comlib.CommandContext;
import me.wilkai.comlib.exception.CommandException;
import me.wilkai.comlib.exception.CommandSyntaxException;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class DoubleArgument extends ArgumentType<Double> {

    public Double parse(String input, CommandContext<?> context) throws CommandException {
        if (input.length() >= 100) {
            throw new CommandSyntaxException("Number too large!");
        }

        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            throw new CommandSyntaxException("Expected a decimal but instead got %s?", input);
        }
    }

    public OptionData suggest(Argument argument) {
        return new OptionData(OptionType.STRING, argument.name(), argument.summary(), argument.required());
    }

}
