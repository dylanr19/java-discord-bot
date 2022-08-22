package me.dylan;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerManager {

    private static PlayerManager INSTANCE;
    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    private PlayerManager() {
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }

    public GuildMusicManager getMusicManager(Guild guild) {
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);
            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());
            return guildMusicManager;
        });
    }
    //TODO: trackLoaded roept nooit op... Er worden alleen playlists gedetecteerd. Deze playlists zijn uiteindelijk maar 1 song, telkens als een song wordt toegevoegd is de playlist dus +1 song.
    //De bot geeft aan dat er een playlist van tientallen songs is gedecteerd terwijl er maar 1 song is toegevoegd aan de playlist. kortom single song worden als playlists gedecteerd.
        public void LoadAndPlay(TextChannel textChannel, String trackURL){
            final GuildMusicManager musicManager = this.getMusicManager(textChannel.getGuild());

            this.audioPlayerManager.loadItemOrdered(musicManager, trackURL, new AudioLoadResultHandler() {
                @Override
                public void trackLoaded(AudioTrack audioTrack) {
//                    musicManager.scheduler.queue(audioTrack);
//                    textChannel.sendMessage("Added to queue: " + audioTrack.getInfo().title).queue();
                }

                @Override
                public void playlistLoaded(AudioPlaylist audioPlaylist) {
                    final List<AudioTrack> tracks = audioPlaylist.getTracks();
                    if(!tracks.isEmpty()){
                        musicManager.scheduler.queue(tracks.get(0));
                        //textChannel.sendMessage("Added to queue: " + tracks.size() + " tracks").queue();
                        textChannel.sendMessage("Added to queue: " + tracks.get(0).getInfo().title).queue();
                    }
                }

                @Override
                public void noMatches() {
                    textChannel.sendMessage("Nothing found by " + trackURL).queue();
                }

                @Override
                public void loadFailed(FriendlyException e) {
                    textChannel.sendMessage("Could not play: " + e.getMessage()).queue();
                }
            });
        }
        public void SkipAndPlayNext(TextChannel textChannel){
            final GuildMusicManager musicManager = this.getMusicManager(textChannel.getGuild());
            musicManager.scheduler.SkipTrack();
        }

        public static PlayerManager getINSTANCE(){
            if(INSTANCE == null){
                INSTANCE = new PlayerManager();
            }

            return INSTANCE;
        }

}
