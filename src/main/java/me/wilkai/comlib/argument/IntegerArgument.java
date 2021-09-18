package me.wilkai.comlib.argument;

import me.wilkai.comlib.Argument;
import me.wilkai.comlib.CommandContext;
import me.wilkai.comlib.exception.CommandException;
import me.wilkai.comlib.exception.CommandSyntaxException;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.math.BigInteger;

public class IntegerArgument extends ArgumentType<Integer> {

    public Integer parse(String input, CommandContext<?> context) throws CommandException {
        if (input.length() > 10) {
            throw new CommandSyntaxException("Number too large!");
        }

        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            try {
                BigInteger value = new BigInteger(input);
                int diff = value.compareTo(BigInteger.valueOf(Integer.MAX_VALUE));

                if (diff > 0) {
                    throw new CommandSyntaxException("Number too small!");
                } else {
                    throw new CommandSyntaxException("Number too large!");
                }
            } catch (NumberFormatException ne) {
                throw new CommandSyntaxException("%s is not a number", input);
            }
        }
    }

    public OptionData suggest(Argument argument) {
        return new OptionData(OptionType.INTEGER, argument.name(), argument.summary(), argument.required());
    }

}
