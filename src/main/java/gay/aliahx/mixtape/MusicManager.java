package gay.aliahx.mixtape;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.util.Map;

public class MusicManager {
    public Map<String, Entry> music;
    public static Map<String, JsonElement> albums;

    public static final Identifier ALBUM_COVERS = new Identifier(Mixtape.MOD_ID, "textures/gui/album_covers.png");

    public MusicManager(JsonObject[] jsonArray) {
        music = Maps.newHashMap();
        albums = Maps.newHashMap();

        for (Map.Entry<String, JsonElement> entry : jsonArray[0].entrySet()) {
            music.put(entry.getKey(), new Entry((JsonObject) entry.getValue()));
        }

        for (Map.Entry<String, JsonElement> entry : jsonArray[1].entrySet()) {
            albums.put(entry.getKey(), entry.getValue());
        }
    }


    public Entry getEntry(String name) {
        if(music.containsKey(name)) {
            return music.get(name);
        } else {
            JsonObject object = new JsonObject();
            object.addProperty("name", (name.substring(0, 1).toUpperCase() + name.substring(1)).replaceAll("_", " "));
            object.addProperty("artist", "");
            object.addProperty("album", "Unknown Album");
            return new Entry(object);
        }
    }

    public static class Entry {
        private final String name;
        private final String artist;
        private final String album;

        public Entry(JsonObject json) {
            this.name = JsonHelper.getString(json, "name");
            this.artist = JsonHelper.getString(json, "artist");
            this.album = JsonHelper.getString(json, "album");
        }

        public String getName() {
            return name;
        }
        public String getArtist() {
            return artist;
        }
        public String getAlbum() {
            return album;
        }
    }

    public static class AlbumCover {
        private final int x;
        private final int y;

        public AlbumCover(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void drawIcon(DrawContext context, int x, int y) {
            context.drawTexture(ALBUM_COVERS, x, y, 0, this.x * 20, this.y * 20, 20, 20, 100, 100);
        }
    }

    public static AlbumCover getAlbumCover(String albumString) {
        if(albums.containsKey(albumString)) {
            JsonObject object = albums.get(albumString).getAsJsonObject();
            return new AlbumCover(object.get("x").getAsInt(), object.get("y").getAsInt());
        }
        return new AlbumCover(0, 0);
    }
}
