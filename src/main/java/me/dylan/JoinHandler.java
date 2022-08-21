package me.dylan;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

import java.io.IOException;
import java.util.Random;

import java.net.URI;
import java.net.URISyntaxException;

public class JoinHandler extends ListenerAdapter {
    int size; // instance variable voor !testroulette
    boolean rouletteON; // instance variable voor !testroulette
    boolean profanityFilter; // instance variable voor !profanityfilter
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


             String link = String.join(" ", event.getMessage().getContentRaw().toLowerCase().split(" ")).replace("!play ", ""); //haalt alleen de link uit de message
             if (!isURL(link)) {
                 link = "ytsearch:" + link + " audio";
             }

             PlayerManager.getINSTANCE().LoadAndPlay(channel, link); //zoekt de locatie van de song op en speelt deze af
         }


        if(msg.equals("!roulette")){
            rouletteON = false;
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
         Member member = event.getMember();
         AudioChannel voiceChannel = member.getVoiceState().getChannel();
         String chosenMember= "";


         if(msg.equals("!testroulette") && voiceChannel == null){channel.sendMessage("You have to be in a voice channel!").queue();}
         else if(msg.equals("!testroulette") && rouletteON == false){
             size = voiceChannel.getMembers().size();
             rouletteON = true;

             if(voiceChannel == null){
                 channel.sendMessage("You have to be in a voice channel!").queue();
                 return;
             }

             else{
                 channel.sendMessage("<@" + member.getId() + "> choose a user! (type: !pick *user*)").queue();
                 for(int i = 0; i < size; i++){
                     channel.sendMessage("- " + voiceChannel.getMembers().get(i).getUser().getName()).queue();
                 }
             }
         }


         if(msg.contains("!pick") && rouletteON == true){
//TODO: wanneer een persoon de voice chat joined of leaved wordt de else if aangeroepen of krijg ik een out of bounds error.
             for(int i = 0; i < size; i++){
                 if(msg.equals("!pick " + voiceChannel.getMembers().get(i).getUser().getName()) && event.getMember().getUser().getName().equals(member.getUser().getName())){
                     chosenMember = voiceChannel.getMembers().get(i).getUser().getName();
                    // msgReceived = true;
                 }
                 else if(!msg.equals("!testroulette")){
                     channel.sendMessage("This user is not in the same voice chat as yours!").queue();
                     return;
                 }
                 else {
                     channel.sendMessage("You did not choose a user!").queue();
                     return;
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
                 if(!randomMemb.getUser().getName().equals("spite")){
                     randomMemb.kick("сука блять ( better luck next time )").queue();
                 }
                 else{channel.sendMessage("(You can't kick the owner of this server)").queue();}
             }
             else{
                 System.out.println("Kicking member...");
                 channel.sendMessage("<@" + member.getId() + "> !").queue();
                 channel.sendMessage("сука блять ( you have lost !) ").queue();
                 channel.sendMessage("Russian roulette is over!").queue();
                 try {
                     Thread.sleep(10000);
                 } catch (InterruptedException e) {
                     throw new RuntimeException(e);
                 }
                 if(member.getUser().getName().equals("spite")){channel.sendMessage("You can't kick the owner of the server!").queue();}
                 else{member.kick("сука блять ( D O O M E D )").queue();}
             }
                rouletteON = false;
         }







         if(msg.equals("!commands")){ // controleert of de message van de user "!commands" is
             //laat de commands zien
             channel.sendMessage("!join - joins the voice channel\n!leave - leaves the voice channel\n!commands - shows the commands\n!play *nameOfSong* - plays youtube songs\n!roulette - Kicks random user from Guild based on Russian Roulette\n!roulette2 - read desc\n!description *command* - shows the description of a given command").queue();
         }

         if(msg.equals("!description roulette2")){
             channel.sendMessage("Russian roulette is a game where you have to guess who will be the doomer.\nThe doomer is randomly chosen from the voice channel and you have to guess who will be the doomer.\nIf you guess correctly, He/She will be kicked from the server.\nIf you guess wrong, you will be banned from the server and you will be the doomer.").queue();
         }

         if(msg.equals("!filter swearing on")){
             channel.sendMessage("The swearing filter is on.\nIf you send a message that contains swearing, it will be filtered.").queue();
             profanityFilter = true;
         }
         else if(msg.equals("!filter swearing off")){
             channel.sendMessage("The swearing filter is off.\nIf you send a message that contains swearing, it will not be filtered.").queue();
             profanityFilter = false;
         }


//TODO: probleem fixen: wanneer je meerdere verschillende swear words verstuurd in de chat worden alleen het eerst gedecteerde swear word gedetecteerd en gefiltered.
         if(profanityFilter){
             String replacedMsg = "";
             String author = "";
             boolean containsSwear = false;
             try {
                 Dictionary dictionary = new Dictionary();
                 for(String words : dictionary.getSwearWords()){
                     if(msg.contains(words)){
                         containsSwear = true;
                         replacedMsg = msg.replace(words, "BOBBA");
                         author = event.getAuthor().getName();
                         event.getMessage().delete().queue();
                     }
                 }
             } catch (IOException e) {
                 throw new RuntimeException(e);
             }
             if(containsSwear){channel.sendMessage(author + ": " + replacedMsg).queue();}
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
