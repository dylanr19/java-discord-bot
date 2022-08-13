package me.dylan;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

import java.net.URI;
import java.net.URISyntaxException;

public class JoinHandler extends ListenerAdapter {
     @Override
     public void onMessageReceived(MessageReceivedEvent event){
         //Controleert of de MessageEvent niet van een bot komt
         if(event.getAuthor().isBot()){
             return;
         }
         String msg = event.getMessage().getContentRaw().toLowerCase(); //Zet de message String in een variabele
         TextChannel channel = event.getChannel().asTextChannel(); //Zet de text channel waarin de message event voorkomt in een variabele

         if(msg.equals("!join")){ // controleert of de message van de user "!join" is
             //join de channel
             if(!event.getGuild().getSelfMember().hasPermission(channel, Permission.VOICE_CONNECT)){ //controleert of de bot de permissie VOICE_CONNECT heeft
                channel.sendMessage("I don't have permission to join that channel!").queue();
                return;
             }

             AudioChannel connectedChannel = event.getMember().getVoiceState().getChannel(); //Zet de voice channel waarin de user zich bevindt in een variabele
                if(connectedChannel == null){ //Controleert of de user in een voice channel zit
                    channel.sendMessage("You are not in a voice channel!").queue();
                    return;
                }

                AudioManager audioManager = event.getGuild().getAudioManager(); //Zet de audio manager in een variabele
                if(audioManager.isConnected()){ //Controleert of de bot al in een voice channel zit
                    channel.sendMessage("The bot is already in your channel! Enter the chill zone!").queue();
                    return;
                }

                audioManager.openAudioConnection(connectedChannel); //Laat de bot verbinden met de voice channel
                channel.sendMessage("Connected to the voice channel!").queue();

         }
         else if(msg.equals("!leave")){ // controleert of de message van de user "!leave" is
             //leave de channel
             AudioChannel connectedChannel = event.getMember().getVoiceState().getChannel(); //Zet de voice channel waarin de user zich bevindt in een variabele
             if(connectedChannel == null){ //Controleert of de user in een voice channel zit
                 channel.sendMessage("You are not in a voice channel!").queue();
                 return;
             }


             event.getGuild().getAudioManager().closeAudioConnection(); //Laat de bot de verbinding met de voice channel verbreken
             channel.sendMessage("Disconnected from the voice channel!").queue();
         }

         if(msg.contains("!play")) {
             System.out.println("Playing song");
             final AudioManager audioManager = event.getGuild().getAudioManager();
             final AudioChannel memberChannel = event.getMember().getVoiceState().getChannel();

             audioManager.openAudioConnection(memberChannel);


             String link = String.join(" ", event.getMessage().toString().split(" ")).replace("!play ", "");

             if (!isURL(link)) {
                 link = "ytsearch:" + link + " audio";
             }

             PlayerManager.getINSTANCE().LoadAndPlay(channel, link);
         }


         if(msg.equals("!commands")){ // controleert of de message van de user "!commands" is
             //laat de commands zien
             channel.sendMessage("!join - joins the voice channel\n!leave - leaves the voice channel\n!commands - shows the commands").queue();
         }

     }

        public boolean isURL(String url){
            try{
                new URI(url);
                return true;
            }catch (URISyntaxException e){
                return false;
            }
        }
}
