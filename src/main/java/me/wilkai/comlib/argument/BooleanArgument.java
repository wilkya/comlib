package me.wilkai.comlib.argument;

import me.wilkai.comlib.Argument;
import me.wilkai.comlib.CommandContext;
import me.wilkai.comlib.exception.CommandException;
import me.wilkai.comlib.exception.CommandSyntaxException;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class BooleanArgument extends ArgumentType<Boolean> {

    public Boolean parse(String input, CommandContext<?> context) throws CommandException {
        String[] ayes = new String[]{"true", "yes", "enable", "on", "vrai"};
        String[] noes = new String[]{"false", "no", "disable", "off", "faux"};

        for (String aye : ayes) {
            if (input.equalsIgnoreCase(aye)) {
                return true;
            }
        }
        for (String no : noes) {
            if (input.equalsIgnoreCase(no)) {
                return false;
            }
        }

        throw new CommandSyntaxException("%s is not a boolean value.", input);
    }

    public OptionData suggest(Argument argument) {
        return new OptionData(OptionType.BOOLEAN, argument.name(), argument.summary(), argument.required());
    }

}
