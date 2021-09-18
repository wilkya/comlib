package me.wilkai.comlib;

import me.wilkai.comlib.argument.ArgumentType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public class CommandSuggester {

    private final Comlib comlib;
    private final Logger logger;

    public CommandSuggester(Comlib comlib) {
        this.comlib = comlib;
        this.logger = JDALogger.getLog("comlib");
    }

    public void suggest(CommandWrapper command) {
        if (command.type == CommandWrapper.CommandType.MESSAGE) return;

        CommandData suggestion = new CommandData(command.name, command.summary);
        suggestion.setDefaultEnabled(!command.isRestricted);

        for (Field argument : command.executor.getFields()) {
            Argument argumentInfo = argument.getAnnotation(Argument.class);

            if (argumentInfo != null) {
                String name = argument.getType().getName();

                switch (name) {
                    case "boolean" -> name = "java.lang.Boolean";
                    case "byte" -> name = "java.lang.Byte";
                    case "short" -> name = "java.lang.Short";
                    case "int" -> name = "java.lang.Integer";
                    case "long" -> name = "java.lang.Long";
                    case "float" -> name = "java.lang.Float";
                    case "double" -> name = "java.lang.Double";
                }

                for (ArgumentType<?> argumentType : comlib.getArgumentTypes()) {
                    // Figure out what Type this ArgumentType handles.
                    Class<?> handledType = (Class<?>) ((ParameterizedType) argumentType.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

                    // If it handles the Type we are trying to parse.
                    if (handledType.getName().equals(name)) {
                        suggestion.addOptions(argumentType.suggest(argumentInfo));
                    }
                }
            }
        }

        List<Guild> guilds = comlib.getJda().getGuilds();

        for(Guild guild : guilds) {
            guild.upsertCommand(suggestion).queue();
        }
        logger.debug("Updated Suggestion for /" + suggestion.getName() + ".");
    }

    public void suggest(CommandGroupWrapper group) {
        CommandData suggestion = new CommandData(group.name, group.summary);
        suggestion.setDefaultEnabled(!group.restricted);

        for (CommandWrapper command : group.commands) {
            SubcommandData subcommand = new SubcommandData(command.name, command.summary);

            for (Field argument : command.executor.getFields()) {
                Argument argumentInfo = argument.getAnnotation(Argument.class);

                if (argumentInfo != null) {
                    String name = argument.getType().getName();

                    switch (name) {
                        case "boolean" -> name = "java.lang.Boolean";
                        case "byte" -> name = "java.lang.Byte";
                        case "short" -> name = "java.lang.Short";
                        case "int" -> name = "java.lang.Integer";
                        case "long" -> name = "java.lang.Long";
                        case "float" -> name = "java.lang.Float";
                        case "double" -> name = "java.lang.Double";
                    }

                    for (ArgumentType<?> argumentType : comlib.getArgumentTypes()) {
                        // Figure out what Type this ArgumentType handles.
                        Class<?> handledType = (Class<?>) ((ParameterizedType) argumentType.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

                        // If it handles the Type we are trying to parse.
                        if (handledType.getName().equals(name)) {
                            OptionData data = argumentType.suggest(argumentInfo);
                            subcommand.addOptions(data);
                        }
                    }
                }
            }

            suggestion.addSubcommands(subcommand);

            List<Guild> guilds = comlib.getJda().getGuilds();

            for(Guild guild : guilds) {
                guild.upsertCommand(suggestion).queue();
            }
            logger.debug("Updated Suggestion for /" + suggestion.getName() + ".");
        }
    }
}