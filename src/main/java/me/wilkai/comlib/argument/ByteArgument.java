package me.wilkai.comlib.argument;

import me.wilkai.comlib.Argument;
import me.wilkai.comlib.CommandContext;
import me.wilkai.comlib.exception.CommandException;
import me.wilkai.comlib.exception.CommandSyntaxException;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.math.BigInteger;

public class ByteArgument extends ArgumentType<Byte> {

    public Byte parse(String input, CommandContext<?> context) throws CommandException {
        if (input.length() > 3) {
            throw new CommandSyntaxException("Not a Byte.");
        }

        try {
            return Byte.parseByte(input);
        } catch (NumberFormatException e) {
            try {
                BigInteger value = new BigInteger(input);
                int diff = value.compareTo(BigInteger.valueOf(Byte.MAX_VALUE));

                if (diff > 0) {
                    throw new CommandSyntaxException("Byte too large!");
                } else {
                    throw new CommandSyntaxException("Byte too small!");
                }
            } catch (NumberFormatException ne) {
                throw new CommandSyntaxException("%s is not a number.", input);
            }
        }
    }

    public OptionData suggest(Argument argument) {
        return new OptionData(OptionType.INTEGER, argument.name(), argument.summary(), argument.required());
    }
}
