package me.wilkai.comlib;

public class CommandWrapper {

    public String name;
    public String summary;
    public boolean isRestricted;
    public boolean isHidden;
    public CommandType type;
    public Class<? extends Command<?>> executor;

    public CommandWrapper setName(String name) {
        this.name = name;
        return this;
    }

    public CommandWrapper setSummary(String summary) {
        this.summary = summary;
        return this;
    }

    public CommandWrapper setRestricted(boolean restricted) {
        this.isRestricted = restricted;
        return this;
    }

    public CommandWrapper setHidden(boolean hidden) {
        this.isHidden = hidden;
        return this;
    }

    public CommandWrapper setExecutor(Class<? extends Command<?>> executor) {
        this.executor = executor;
        return this;
    }

    public CommandWrapper setType(CommandType type) {
        this.type = type;
        return this;
    }

    /**
     * Checks if a given input matches either the name or an alias of this Command.
     *
     * @param command The input to check.
     * @return True if a match is found, False otherwise.
     */
    public boolean matches(String command) {
        return command.equalsIgnoreCase(name);
    }

    public enum CommandType {
        MESSAGE,
        SLASH,
        BOTH
    }

}
