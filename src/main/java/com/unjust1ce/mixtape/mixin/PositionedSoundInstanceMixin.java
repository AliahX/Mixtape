package com.unjust1ce.mixtape.mixin;

import com.unjust1ce.mixtape.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;


@Mixin(PositionedSoundInstance.class)
public class PositionedSoundInstanceMixin{

    @Inject(method = "music(Lnet/minecraft/sound/SoundEvent;)Lnet/minecraft/client/sound/PositionedSoundInstance;", at = @At("RETURN"), cancellable = true)
    private static void musicMixin(SoundEvent sound, CallbackInfoReturnable<PositionedSoundInstance> cir) {
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        float volume = 100.0F;
        if (config.mainConfig.enabled && config.mainConfig.varyPitch) {
            switch (sound.getId().toString()) {
                case "minecraft:music.menu" -> {
                    if (!config.menuConfig.enabled) cir.setReturnValue(null);
                    volume = config.menuConfig.volume;
                }
                case "minecraft:music.creative" -> {
                    if (!config.creativeConfig.enabled) cir.setReturnValue(null);
                    volume = config.creativeConfig.volume;
                }
                case "minecraft:music.end" -> {
                    if (!config.endConfig.enabled) cir.setReturnValue(null);
                    volume = config.endConfig.volume;
                }
                case "minecraft:music.underwater" -> {
                    if (!config.underwaterConfig.enabled) cir.setReturnValue(null);
                    volume = config.underwaterConfig.volume;
                }
                case "minecraft:music.credits" -> {
                    if (!config.creditsConfig.enabled) cir.setReturnValue(null);
                    volume = config.creditsConfig.volume;
                }
                case "minecraft:music.game" -> {
                    if (!config.gameConfig.enabled) cir.setReturnValue(null);
                    volume = config.gameConfig.volume;
                }
                case "minecraft:music.nether.nether_wastes", "minecraft:music.nether.warped_forest", "minecraft:music.nether.soul_sand_valley", "minecraft:music.nether.crimson_forest", "minecraft:music.nether.basalt_deltas" -> {
                    if (!config.netherConfig.enabled) cir.setReturnValue(null);
                    volume = config.netherConfig.volume;
                }
            }
            Random random = new Random();
            long note = random.nextLong((config.mainConfig.maxNoteChange - config.mainConfig.minNoteChange) + 1) + config.mainConfig.minNoteChange;
            cir.setReturnValue(new PositionedSoundInstance(sound.getId(), SoundCategory.MUSIC, volume / 100, (float) Math.pow(2.0D, (double) (note) / 12.0D), SoundInstance.createRandom(), false, 0, SoundInstance.AttenuationType.NONE, 0.0D, 0.0D, 0.0D, false));
        }
    }

    @Inject(method = "record", at = @At("RETURN"), cancellable = true)
    private static void recordMixin(SoundEvent sound, double x, double y, double z, CallbackInfoReturnable<PositionedSoundInstance> cir) {
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        if(config.mainConfig.enabled && config.jukeboxConfig.dogReplacesCat && sound.getId().toString().equals("minecraft:music_disc.cat")) {
            sound = new SoundEvent(new Identifier("mixtape:music.dog"));
        }

        if(config.mainConfig.enabled && config.jukeboxConfig.elevenReplaces11 && sound.getId().toString().equals("minecraft:music_disc.11")) {
            sound = new SoundEvent(new Identifier("mixtape:music.eleven"));
        }

        if (config.mainConfig.enabled && config.jukeboxConfig.mono) {
            cir.setReturnValue(new PositionedSoundInstance(sound.getId(), SoundCategory.RECORDS, config.jukeboxConfig.volume / 100, 1.0F, SoundInstance.createRandom(), false, 0, SoundInstance.AttenuationType.LINEAR, 0, 0, 0, true));
        } else {
            cir.setReturnValue(new PositionedSoundInstance(sound.getId(), SoundCategory.RECORDS, config.jukeboxConfig.volume / 100, 1.0F, SoundInstance.createRandom(), false, 0, SoundInstance.AttenuationType.LINEAR, x, y, z, false));
        }
    }
}
