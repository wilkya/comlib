package me.wilkai.comlib;

import me.wilkai.comlib.argument.ArgumentType;
import me.wilkai.comlib.exception.CommandException;
import me.wilkai.comlib.exception.CommandSyntaxException;
import me.wilkai.comlib.util.StringUtils;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

/**
 * Class which handles Command Execution and Slash Command Suggestions.
 */
@SuppressWarnings("unchecked") // smh
public class CommandDispatcher {

    // private final JDALogger logger;

    private final Comlib comlib;

    /**
     * Class which sends Command Autocompletions "Suggestions" to Discord.
     */
    private final CommandSuggester suggester;

    /**
     * <br> A map containing the name of Java primitives and their respective non-primitive classes.
     * <br> This is because primitives seem to confuse Java.
     */
    private final HashMap<String, String> primitives;

    /**
     * Creates a new instance of the Command Dispatcher class.
     */
    public CommandDispatcher() {
        this.comlib = Comlib.getInstance();
        this.suggester = new CommandSuggester(comlib);
        this.primitives = new HashMap<>();

        primitives.put("boolean","java.lang.Boolean");
        primitives.put("byte","java.lang.Byte");
        primitives.put("short","java.lang.Short");
        primitives.put("int","java.lang.Integer");
        primitives.put("long","java.lang.Long");
        primitives.put("float","java.lang.Float");
        primitives.put("double","java.lang.Double");
    }

    /**
     * Sends autocompletions for all installed commands.
     */
    public void sendSuggestions() {
        for (CommandWrapper command : comlib.getCommands()) {
            suggester.suggest(command);
        }

        for (CommandGroupWrapper group : comlib.getGroups()) {
            suggester.suggest(group);
        }
    }

    /**
     * Executes a requested Command.
     *
     * @param event The Command that was sent from Discord. (Caught by the DiscordBot class.)
     */
    public void dispatch(SlashCommandEvent event) {
        String name = event.getName();

        for (CommandWrapper command : comlib.getCommands()) {
            if (command.matches(name) && command.type != CommandWrapper.CommandType.MESSAGE) {
                SlashCommandContext context = new SlashCommandContext(event);

                event.deferReply(command.isHidden).queue();
                this.execute((Class<? extends Command<SlashCommandContext>>) command.executor, event.getOptions(), context);
                return;
            }
        }

        for (CommandGroupWrapper group : comlib.getGroups()) {
            if (group.name.equalsIgnoreCase(event.getName())) {

                if (event.getSubcommandName() == null) {
                    break;
                }

                for (CommandWrapper command : group.commands) {
                    if (command.matches(event.getSubcommandName())) {
                        SlashCommandContext context = new SlashCommandContext(event);

                        if (command.type == CommandWrapper.CommandType.MESSAGE) {
                            context.reply("/" + command.name + " does not support Slash Commands.");
                            return;
                        }

                        this.execute((Class<? extends Command<SlashCommandContext>>) command.executor, event.getOptions(), context);
                        return;
                    }
                }
            }
        }

        // We could not find a matching command.
        String message = String.format("The command */%s* seems to have been removed.\nIt has now been removed from the list of Command Suggestions.", event.getName());
        event.reply(message).setEphemeral(true).queue();
    }

    /**
     * Check if a Message is a command and handle it if it is.
     * @param event The event containing the received message.
     */
    public void dispatch(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();

        if (!message.startsWith("\\") || event.getAuthor().isBot()) {
            return;
        }

        List<String> args = StringUtils.quotedSplit(message, ' ');

        if (args == null) return;

        String name = args.get(0).replace("\\", "");
        args.remove(0);

        for (CommandWrapper command : comlib.getCommands()) {
            if (command.matches(name)) {
                MessageCommandContext context = new MessageCommandContext(event);

                if (command.type == CommandWrapper.CommandType.SLASH) {
                    context.reply("/" + command.name + " does not support Legacy Commands.");
                    return;
                }

                this.execute(command, (Class<? extends Command<MessageCommandContext>>) command.executor, args, context);
                return;
            }
        }

        for (CommandGroupWrapper group : comlib.getGroups()) {
            if (group.name.equalsIgnoreCase(name)) {

                String subcommand = args.get(1);
                args.remove(1);

                for (CommandWrapper command : group.commands) {
                    if (command.matches(subcommand)) {
                        MessageCommandContext context = new MessageCommandContext(event);

                        this.execute(command, (Class<? extends Command<MessageCommandContext>>) command.executor, args, context);
                        return;
                    }
                }
            }
        }
    }

    /**
     * Executes a specific command.
     *
     * @param executor  The class which will execute the Command.
     * @param arguments The arguments which were sent from Discord.
     * @param context   The context in which the command was sent.
     */
    private void execute(Class<? extends Command<SlashCommandContext>> executor, List<OptionMapping> arguments, SlashCommandContext context) {
        try {
            Command<SlashCommandContext> command = executor.getConstructor().newInstance();
            command.context = context;

            // Stores how much of the Command we have executed so we can tell a user exactly where an issue is if a Syntax error occurs.
            StringBuilder progress = new StringBuilder("/" + context.event.getName());

            // Loop through all the provided Arguments.
            for (OptionMapping option : arguments) {
                Field arg = null;

                for(Field field : command.getClass().getFields()) {
                    Argument argument = field.getAnnotation(Argument.class);

                    if(argument != null) {
                        if(argument.name().equals(option.getName())) {
                            arg = field;
                        }
                    }
                }

                if(arg == null) continue;

                Class<?> type = arg.getType();
                String name = type.getName();

                if(primitives.containsKey(name)) {
                    name = this.primitives.get(name);
                }

                Object value = null;
                progress.append(" ").append(option.getAsString());

                for (ArgumentType<?> argumentType : comlib.getArgumentTypes()) {
                    // Figure out what Type this ArgumentType handles.
                    Class<?> handledType = (Class<?>) ((ParameterizedType) argumentType.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

                    // If it handles the Type we are trying to parse.
                    if (handledType.getName().equals(name)) {
                        try {
                            value = argumentType.parse(option.getAsString(), context);
                        } catch (CommandSyntaxException e) { // If a Syntax Error occurs.
                            String response = e.getMessage() + "\n"
                                    + "`" + progress + " <-- [Here]`"; // Tell the user exactly where the Error occurred.

                            context.reply(response);

                            return;
                        } catch (CommandException e) { // If a Non-Syntax Error occurs.
                            context.reply(e.getMessage()); // Send the message that the Error provided.
                            return;
                        }
                    }
                }

                arg.set(command, value); // If all is well set the value of the Argument.
            }

            command.onCommand();

        } catch (CommandException e) {
            context.reply(e.getMessage());
        } catch (Exception e) { // If an error occurs at any point.
            context.reply("Something went wrong whilst executing command!");
        }
    }

    public void execute(CommandWrapper instance, Class<? extends Command<MessageCommandContext>> executor, List<String> args, MessageCommandContext context) {
        try {
            Command<MessageCommandContext> command = executor.getConstructor().newInstance();
            command.context = context;

            // Stores how much of the Command we have executed so we can tell a user exactly where an issue is if a Syntax error occurs.
            StringBuilder progress = new StringBuilder("/" + instance.name);

            // Loop through all the provided Arguments.
            for (int i = 0; i < args.size(); i++) {
                Field field = command.getClass().getFields()[i];
                String option = args.get(i);
                Class<?> type = field.getType();
                String name = type.getName();

                if(primitives.containsKey(name)) {
                    name = this.primitives.get(name);
                }

                Object value = null;
                progress.append(" ").append(option);

                for (ArgumentType<?> argumentType : comlib.getArgumentTypes()) {
                    // Figure out what Type this ArgumentType handles.
                    Class<?> handledType = (Class<?>) ((ParameterizedType) argumentType.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

                    // If it handles the Type we are trying to parse.
                    if (handledType.getName().equals(name)) {
                        try {
                            value = argumentType.parse(option, context);
                        } catch (CommandSyntaxException e) { // If a Syntax Error occurs.
                            String response = e.getMessage() + "\n"
                                    + "`" + progress + " <-- [Here]`"; // Tell the user exactly where the Error occurred.

                            context.reply(response);

                            return;
                        } catch (CommandException e) { // If a Non-Syntax Error occurs.
                            context.reply(e.getMessage()); // Send the message that the Error provided.
                            return;
                        }
                    }
                }

                field.set(command, value); // If all is well set the value of the Argument.
            }

            command.onCommand();
        } catch (CommandException e) {
            context.reply(e.getMessage());
        } catch (Exception e) {
            context.reply("Something went wrong whilst executing command!");
        }
    }

    public CommandSuggester getSuggester() {
        return this.suggester;
    }
}