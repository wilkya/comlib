package me.wilkai.comlib;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.GenericEvent;

/**
 * The Context in which a command was executed.
 * Contains a lot of utility fields and methods.
 */
public abstract class CommandContext<E extends GenericEvent> {

    /**
     * The current GSS Server instance.
     */
    public final Comlib comlib;

    /**
     * The User that issued this command.
     */
    public User sender;

    /**
     * The Guild/Server this command was issued in.
     */
    public Guild guild;

    /**
     * The currently running Discord Client.
     */
    public final JDA client;

    /**
     * Gets the channel this command was issued in.
     */
    public MessageChannel channel;

    public final E event;

    /**
     * Creates a new instance of the CommandContext class.
     *
     * @param event The event containing the issued information.
     */
    public CommandContext(E event) {
        this.comlib = Comlib.getInstance();
        this.client = event.getJDA();
        this.event = event;
    }

    /**
     * Sends a basic string message to the channel the command was issued in.
     *
     * @param message The message to send.
     */
    public void reply(String message, Object... args) {
        if (args.length == 0) {
            this.reply(message, false, null);
        } else {
            this.reply(String.format(message, args), false, null);
        }
    }

    /**
     * Sends a basic string message to the channel the command was issued in. (With optional TTS!)
     *
     * @param message The message to send.
     * @param tts     Speak this message aloud?
     */
    public void reply(String message, boolean tts) {
        this.reply(message, tts, null);
    }

    /**
     * Sends a basic string message to the channel the command was issued in along with an embed. (With optional TTS!)
     *
     * @param message The message to send.
     * @param tts     Speak this message aloud?
     * @param embed   The Embed to send.
     */
    public void reply(String message, boolean tts, MessageEmbed embed) {
        MessageBuilder builder = new MessageBuilder();
        builder.setContent(message);
        builder.setTTS(tts);

        if(embed != null) {
            builder.setEmbeds(embed);
        }

        this.reply(builder.build());
    }

    /**
     * Sends a message containing only an Embed.
     *
     * @param embed The MessageEmbed to send.
     */
    public void reply(MessageEmbed embed) {
        this.reply("", false, embed);
    }

    /**
     * Sends a Discord Message to the channel the command was issued in. Very classy.
     *
     * @param message The message to send.
     * @see MessageBuilder
     */
    public abstract void reply(Message message);
}