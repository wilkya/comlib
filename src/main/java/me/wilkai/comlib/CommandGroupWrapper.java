package me.wilkai.comlib;

import java.util.Arrays;
import java.util.List;

public class CommandGroupWrapper {

    public String name;
    public String summary;
    public boolean restricted;
    public List<CommandWrapper> commands;

    public CommandGroupWrapper(String name, String summary, CommandWrapper... commands) {
        this(name, summary, false, commands);
    }

    public CommandGroupWrapper(String name, String summary, boolean restricted, CommandWrapper... commands) {
        this.name = name;
        this.summary = summary;
        this.restricted = false;
        this.commands = Arrays.asList(commands);
    }

}