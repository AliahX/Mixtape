package gay.aliahx.mixtape;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

public class MixtapePacks {
    public static void init() {
        Identifier onlyC418 = Identifier.of("mixtape", "only_c418");
        Identifier onlyC418PlusUnusedMusic = Identifier.of("mixtape", "only_c418_plus_unused_music");
        Identifier noNetherUpdate = Identifier.of("mixtape", "no_nether_update");
        Identifier noNetherUpdatePlusUnusedMusic = Identifier.of("mixtape", "no_nether_update_plus_unused_music");
        Identifier unusedMusic = Identifier.of("mixtape", "unused_music");
        Identifier end0x10c = Identifier.of("mixtape", "0x10c_in_the_end");
        Identifier noBiomeBased = Identifier.of("mixtape", "no_biome_based");
        Identifier noBiomeBasedPlusUnusedMusic = Identifier.of("mixtape", "no_biome_based_plus_unused_music");
        FabricLoader.getInstance().getModContainer("mixtape").ifPresent(container -> ResourceManagerHelper.registerBuiltinResourcePack(onlyC418, container, ResourcePackActivationType.NORMAL));
        FabricLoader.getInstance().getModContainer("mixtape").ifPresent(container -> ResourceManagerHelper.registerBuiltinResourcePack(onlyC418PlusUnusedMusic, container, ResourcePackActivationType.NORMAL));
        FabricLoader.getInstance().getModContainer("mixtape").ifPresent(container -> ResourceManagerHelper.registerBuiltinResourcePack(noNetherUpdate, container, ResourcePackActivationType.NORMAL));
        FabricLoader.getInstance().getModContainer("mixtape").ifPresent(container -> ResourceManagerHelper.registerBuiltinResourcePack(noNetherUpdatePlusUnusedMusic, container, ResourcePackActivationType.NORMAL));
        FabricLoader.getInstance().getModContainer("mixtape").ifPresent(container -> ResourceManagerHelper.registerBuiltinResourcePack(unusedMusic, container, ResourcePackActivationType.NORMAL));
        FabricLoader.getInstance().getModContainer("mixtape").ifPresent(container -> ResourceManagerHelper.registerBuiltinResourcePack(end0x10c, container, ResourcePackActivationType.NORMAL));
        FabricLoader.getInstance().getModContainer("mixtape").ifPresent(container -> ResourceManagerHelper.registerBuiltinResourcePack(noBiomeBased, container, ResourcePackActivationType.NORMAL));
        FabricLoader.getInstance().getModContainer("mixtape").ifPresent(container -> ResourceManagerHelper.registerBuiltinResourcePack(noBiomeBasedPlusUnusedMusic, container, ResourcePackActivationType.NORMAL));
    }
}
