package com.aliahx.mixtape;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.JsonHelper;

import java.util.Map;

public class MusicManager {
    public Map<String, Entry> music = Maps.newHashMap();

    public MusicManager(JsonObject json) {
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            music.put(entry.getKey(), new Entry((JsonObject) entry.getValue()));
        }
    }

    public Entry getEntry(String name) {
        return music.get(name);
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
}
