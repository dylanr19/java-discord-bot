package me.dylan;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import java.util.Random;

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


             String link = String.join(" ", event.getMessage().getContentRaw().toLowerCase().split(" ")).replace("!play ", "");
             if (!isURL(link)) {
                 link = "ytsearch:" + link + " audio";
             }

             PlayerManager.getINSTANCE().LoadAndPlay(channel, link);
         }


        if(msg.equals("!roulette")){
            Guild guild = event.getGuild();
            Member member = event.getMember();
            if(member.getVoiceState().getChannel() == null){
                channel.sendMessage("You have to be in a voice channel!").queue();
                return;
            }
            else{
                System.out.println(member.getVoiceState().getChannel().getName());
                AudioChannel voiceChannel = member.getVoiceState().getChannel();

                int size = voiceChannel.getMembers().size();
                channel.sendMessage("Russian roulette is starting for: ").queue();

                for(int i = 0; i < size; i++){
                    channel.sendMessage("- " + voiceChannel.getMembers().get(i).getUser().getName()).queue();
                }

                channel.sendMessage("Sit tight comrades!").queue();

                Random r = new Random();
                int randomNum = r.nextInt(size);

                Member randomMemb = voiceChannel.getMembers().get(randomNum);
                System.out.println(randomMemb.getUser().getName());
                channel.sendMessage("The doomer is... ").queue();

                for(int i = 0; i < 5; i++){
                    channel.sendMessage(".").queue();

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }

                channel.sendMessage("<@" + randomMemb.getId() + "> !").queue();
                channel.sendMessage("сука блять ( better luck next time! ) ").queue();
                channel.sendMessage("Russian roulette is over!").queue();


                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Kicking member...");
                randomMemb.kick("сука блять ( better luck next time )").queue();
                }

            }

         if(msg.equals("!testroulette")){
             Member member = event.getMember();
             AudioChannel voiceChannel = member.getVoiceState().getChannel();
             Guild guild = event.getGuild();
             int size = voiceChannel.getMembers().size();

             if(member.getVoiceState().getChannel() == null){
                 channel.sendMessage("You have to be in a voice channel!").queue();
                 return;
             }
             else{
                 channel.sendMessage("<@" + member.getId() + "> choose a user!").queue();
                 for(int i = 0; i < size; i++){
                     channel.sendMessage("- " + voiceChannel.getMembers().get(i).getUser().getName()).queue();
                 }

//                 try {
//                     Thread.sleep(10000);
//                 } catch (InterruptedException e) {
//                     throw new RuntimeException(e);
//                 }
                 String chosenMember= "";
                 boolean msgReceived = false;
                 final String[] LatestMsg = {""};
                 while(!msgReceived){
                     //System.out.println("Waiting for message...");
                     for(int i = 0; i < size; i++){
        //TODO: while loop veroorzaakt een memory overloading.
                         channel.getHistory().retrievePast(1).queue(messages -> {
                             // messages (list) contains all received messages
                             // Access them in here
                             // Use for example messages.get(0) to get the received message
                             // (messages is of type List)
                             int historySize = messages.size();
                             LatestMsg[0] = messages.get(historySize - 1).getContentRaw();

                         });
                         //System.out.println(LatestMsg[0]);
                         if(LatestMsg[0].equals("!" + voiceChannel.getMembers().get(i).getUser().getName()) && event.getMember().getUser().getName().equals(member.getUser().getName())){
                             chosenMember = voiceChannel.getMembers().get(i).getUser().getName();
                             msgReceived = true;
                         }
                         else if(!msg.equals("!testroulette")){
                             channel.sendMessage("This user is not in the same voice chat as yours!").queue();
                             return;
                         }
                     }
                 }

                         channel.sendMessage("Russian roulette is starting!").queue();
                         channel.sendMessage("Sit tight comrades!").queue();

                         Random r = new Random();
                         int randomNum = r.nextInt(size);

                         Member randomMemb = voiceChannel.getMembers().get(randomNum);
                         System.out.println(randomMemb.getUser().getName());
                         channel.sendMessage("The doomer is... ").queue();

                         for(int y = 0; y < 5; y++){
                             channel.sendMessage(".").queue();

                             try {
                                 Thread.sleep(1000);
                             } catch (InterruptedException e) {
                                 throw new RuntimeException(e);
                             }

                         }


//TODO: OpenJDK 64-Bit Server VM warning: Exception java.lang.OutOfMemoryError occurred dispatching signal UNKNOWN to handler- the VM may need to be forcibly terminated, bij 2e keer roulette2 runnen

                         if(chosenMember.equals(randomMemb.getNickname())){
                             System.out.println("Kicking member...");
                             channel.sendMessage("<@" + randomMemb.getId() + "> !").queue();
                             channel.sendMessage("сука блять ( better luck next time! ) ").queue();
                             channel.sendMessage("Russian roulette is over!").queue();
                             try {
                                 Thread.sleep(10000);
                             } catch (InterruptedException e) {
                                 throw new RuntimeException(e);
                             }
                             randomMemb.kick("сука блять ( better luck next time )").queue();
                         }
                         else{
                             System.out.println("Kicking member...");
                             channel.sendMessage("<@" + member.getId() + "> !").queue();
                             channel.sendMessage("сука блять ( you are d o o m e d !) ").queue();
                             channel.sendMessage("Russian roulette is over!").queue();
                             try {
                                 Thread.sleep(10000);
                             } catch (InterruptedException e) {
                                 throw new RuntimeException(e);
                             }
                             member.kick("сука блять ( D O O M E D )").queue();
                         }



             }

         }







         if(msg.equals("!commands")){ // controleert of de message van de user "!commands" is
             //laat de commands zien
             channel.sendMessage("!join - joins the voice channel\n!leave - leaves the voice channel\n!commands - shows the commands\n!play *nameOfSong* - plays youtube songs\n!roulette - Kicks random user from Guild based on Russian Roulette\n!description *command* - shows the description of a given command").queue();
         }

         if(msg.equals("!description roulette")){
             channel.sendMessage("Russian roulette is a game where you have to guess who will be the doomer.\nThe doomer is randomly chosen from the voice channel and you have to guess who will be the doomer.\nIf you guess correctly, He/She will be kicked from the server.\nIf you guess wrong, you will be banned from the server and you will be the doomer.").queue();
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
