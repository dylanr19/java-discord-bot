package me.dylan;

import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import javax.security.auth.login.LoginException;
import java.util.EnumSet;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class DiscordBot {
    public static void main(String[] args) throws LoginException, InterruptedException {
        if(args.length == 0){
            System.err.println("Unable to start without token!");
            System.exit(1);
        }
        String token = args[0];

        // We only need 3 gateway intents enabled for this example:
        EnumSet<GatewayIntent> intents = EnumSet.of(
                // We need messages in guilds to accept commands from users
                GatewayIntent.GUILD_MESSAGES,
                // We need voice states to connect to the voice channel
                GatewayIntent.GUILD_VOICE_STATES,
                // Enable access to message.getContentRaw()
                GatewayIntent.MESSAGE_CONTENT
        );


        JDA bot = JDABuilder.create(token, intents)
                .enableCache(CacheFlag.VOICE_STATE)
                .setActivity(Activity.playing("Type: !commands"))
                .addEventListeners(new JoinHandler())
                .build();


        System.out.println("Logged in as " + bot.getSelfUser().getName() + "#" + bot.getSelfUser().getDiscriminator() + "!");

        //TODO: dit werkend krijgen? er worden geen guilds op het scherm geprint.
        System.out.println("Guilds: ");
        bot.getGuilds().forEach(guild -> {
            System.out.println(guild.getName());
        });
    }
}