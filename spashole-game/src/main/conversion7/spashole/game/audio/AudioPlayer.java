package conversion7.spashole.game.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.ObjectMap;

public class AudioPlayer {

    private static ObjectMap<String, Music> activeTracks = new ObjectMap<>();

    public static Music play(String path) {
        Music active = AudioPlayer.activeTracks.get(path);
        if (active != null) {
            active.stop();
            active.play();
            return active;
        }

        Music music = Gdx.audio.newMusic(Gdx.files.internal(path));
        music.play();
        AudioPlayer.activeTracks.put(path, music);
        music.setOnCompletionListener(m -> {
            m.dispose();
            AudioPlayer.activeTracks.remove(path);
        });
        return music;
    }

    public static void stopAll() {
        for (ObjectMap.Entry<String, Music> musicEntry : activeTracks) {
            musicEntry.value.stop();
            musicEntry.value.dispose();
        }
        activeTracks.clear();
    }
}
