package com.unjust1ce.mixtape.config;

import com.unjust1ce.mixtape.Mixtape;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

public class MixtapePacks {
    public static void init() {
        Identifier packId = new Identifier("mixtape", "cassettes");
        FabricLoader.getInstance().getModContainer("mixtape").ifPresent(container
                -> ResourceManagerHelper.registerBuiltinResourcePack(packId, container, ResourcePackActivationType.DEFAULT_ENABLED));
    }
}
