package me.wilkai.comlib;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MessageCommandContext extends CommandContext<MessageReceivedEvent> {

    public final Message message;

    /**
     * Creates a new instance of the CommandContext class.
     *
     * @param event The event containing the issued message.
     */
    public MessageCommandContext(MessageReceivedEvent event) {
        super(event);
        this.sender = event.getAuthor();
        this.channel = event.getChannel();
        this.guild = event.getGuild();
        this.message = event.getMessage();
    }

    public void reply(Message message) {
        this.message.reply(message).mentionRepliedUser(false).queue();
    }
}