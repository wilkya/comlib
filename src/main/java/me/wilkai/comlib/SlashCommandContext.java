package me.wilkai.comlib;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class SlashCommandContext extends CommandContext<SlashCommandEvent> {

    /**
     * Creates a new instance of the CommandContext class.
     *
     * @param event The event containing the issued command.
     */
    public SlashCommandContext(SlashCommandEvent event) {
        super(event);
        this.sender = event.getUser();
        this.channel = event.getChannel();
        this.guild = event.getGuild();
    }

    public void reply(Message message) {
        this.reply(message, false);
    }

    public void reply(Message message, boolean ephemeral) {
        this.event.getHook().sendMessage(message).setEphemeral(ephemeral).queue();
    }
}