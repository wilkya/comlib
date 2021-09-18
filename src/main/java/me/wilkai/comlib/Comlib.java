package me.wilkai.comlib;

import me.wilkai.comlib.argument.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Comlib extends ListenerAdapter {

    private static Comlib INSTANCE;

    private final JDA jda;

    private final List<CommandWrapper> commands;
    private final List<CommandGroupWrapper> groups;
    private final List<ArgumentType<?>> argumentTypes;

    private final CommandDispatcher dispatcher;

    public Comlib(JDA jda) {
        INSTANCE = this;

        this.jda = jda;
        this.commands = new ArrayList<>();
        this.groups = new ArrayList<>();
        this.argumentTypes = new ArrayList<>();
        this.dispatcher = new CommandDispatcher();

        this.addArgumentTypes(
                new BooleanArgument(),
                new ByteArgument(),
                new ShortArgument(),
                new IntegerArgument(),
                new LongArgument(),
                new FloatArgument(),
                new DoubleArgument(),
                new StringArgument()
        );

        jda.addEventListener(this);
    }

    public Comlib addCommand(CommandWrapper command) {
        this.commands.add(command);
        return this;
    }

    public Comlib addCommands(CommandWrapper... commands) {
        this.commands.addAll(Arrays.asList(commands));
        return this;
    }

    public Comlib addCommandGroup(CommandGroupWrapper group) {
        this.groups.add(group);
        return this;
    }

    public Comlib addCommandGroups(CommandGroupWrapper... groups) {
        this.groups.addAll(Arrays.asList(groups));
        return this;
    }

    public Comlib addArgumentType(ArgumentType<?> argumentType) {
        this.argumentTypes.add(argumentType);
        return this;
    }

    public Comlib addArgumentTypes(ArgumentType<?>... argumentTypes) {
        this.argumentTypes.addAll(Arrays.asList(argumentTypes));
        return this;
    }

    public CommandDispatcher getDispatcher() {
        return this.dispatcher;
    }

    public List<CommandWrapper> getCommands() {
        return commands;
    }

    public List<CommandGroupWrapper> getGroups() {
        return groups;
    }

    public List<ArgumentType<?>> getArgumentTypes() {
        return argumentTypes;
    }

    public JDA getJda() {
        return this.jda;
    }

    public static Comlib getInstance() {
        return INSTANCE;
    }

    // Events
    public void onReady(@NotNull ReadyEvent event) {
        this.dispatcher.sendSuggestions();
    }

    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        this.dispatcher.dispatch(event);
    }

    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        this.dispatcher.dispatch(event);
    }
}
