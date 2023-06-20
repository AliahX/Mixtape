package com.aliahx.mixtape.mixin;

import com.aliahx.mixtape.Mixtape;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

import static com.aliahx.mixtape.Mixtape.config;

@Mixin(PositionedSoundInstance.class)
public class PositionedSoundInstanceMixin {

    @Inject(method = "music(Lnet/minecraft/sound/SoundEvent;)Lnet/minecraft/client/sound/PositionedSoundInstance;", at = @At("RETURN"), cancellable = true)
    private static void musicMixin(SoundEvent sound, CallbackInfoReturnable<PositionedSoundInstance> cir) {
        if(config.mainConfig.enabled) {
            Mixtape.debugCurrentMusicType = sound.getId().toString();
            sound = switch (config.mainConfig.musicType) {
                case AUTOMATIC -> sound;
                case CREATIVE -> SoundEvents.MUSIC_CREATIVE.value();
                case CREDITS -> SoundEvents.MUSIC_CREDITS.value();
                case DRAGON -> SoundEvents.MUSIC_DRAGON.value();
                case END -> SoundEvents.MUSIC_END.value();
                case GAME -> SoundEvents.MUSIC_GAME.value();
                case MENU -> SoundEvents.MUSIC_MENU.value();
                case NETHER_BASALT_DELTAS -> SoundEvents.MUSIC_NETHER_BASALT_DELTAS.value();
                case NETHER_CRIMSON_FOREST -> SoundEvents.MUSIC_NETHER_CRIMSON_FOREST.value();
                case OVERWORLD_DEEP_DARK -> SoundEvents.MUSIC_OVERWORLD_DEEP_DARK.value();
                case OVERWORLD_DRIPSTONE_CAVES -> SoundEvents.MUSIC_OVERWORLD_DRIPSTONE_CAVES.value();
                case OVERWORLD_GROVE -> SoundEvents.MUSIC_OVERWORLD_GROVE.value();
                case OVERWORLD_JAGGED_PEAKS -> SoundEvents.MUSIC_OVERWORLD_JAGGED_PEAKS.value();
                case OVERWORLD_LUSH_CAVES -> SoundEvents.MUSIC_OVERWORLD_LUSH_CAVES.value();
                case OVERWORLD_SWAMP -> SoundEvents.MUSIC_OVERWORLD_SWAMP.value();
                case OVERWORLD_FOREST -> SoundEvents.MUSIC_OVERWORLD_FOREST.value();
                case OVERWORLD_OLD_GROWTH_TAIGA -> SoundEvents.MUSIC_OVERWORLD_OLD_GROWTH_TAIGA.value();
                case OVERWORLD_MEADOW -> SoundEvents.MUSIC_OVERWORLD_MEADOW.value();
                case OVERWORLD_CHERRY_GROVE -> SoundEvents.MUSIC_OVERWORLD_CHERRY_GROVE.value();
                case NETHER_NETHER_WASTES -> SoundEvents.MUSIC_NETHER_NETHER_WASTES.value();
                case OVERWORLD_FROZEN_PEAKS -> SoundEvents.MUSIC_OVERWORLD_FROZEN_PEAKS.value();
                case OVERWORLD_SNOWY_SLOPES -> SoundEvents.MUSIC_OVERWORLD_SNOWY_SLOPES.value();
                case NETHER_SOUL_SAND_VALLEY -> SoundEvents.MUSIC_NETHER_SOUL_SAND_VALLEY.value();
                case OVERWORLD_STONY_PEAKS -> SoundEvents.MUSIC_OVERWORLD_STONY_PEAKS.value();
                case NETHER_WARPED_FOREST -> SoundEvents.MUSIC_NETHER_WARPED_FOREST.value();
                case OVERWORLD_FLOWER_FOREST -> SoundEvents.MUSIC_OVERWORLD_FLOWER_FOREST.value();
                case OVERWORLD_DESERT -> SoundEvents.MUSIC_OVERWORLD_DESERT.value();
                case OVERWORLD_BADLANDS -> SoundEvents.MUSIC_OVERWORLD_BADLANDS.value();
                case OVERWORLD_JUNGLE -> SoundEvents.MUSIC_OVERWORLD_JUNGLE.value();
                case OVERWORLD_SPARSE_JUNGLE -> SoundEvents.MUSIC_OVERWORLD_SPARSE_JUNGLE.value();
                case OVERWORLD_BAMBOO_JUNGLE -> SoundEvents.MUSIC_OVERWORLD_BAMBOO_JUNGLE.value();
                case UNDER_WATER -> SoundEvents.MUSIC_UNDER_WATER.value();
            };

            if(config.gameConfig.creativeMusicPlaysInSurvival && (sound.getId().toString().equals("minecraft:music.game") || sound.getId().toString().contains("minecraft:music.overworld."))) {
                sound = SoundEvents.MUSIC_CREATIVE.value();
            }

            float volume = switch (sound.getId().toString()) {
                case "minecraft:music.menu" ->  config.menuConfig.volume;
                case "minecraft:music.creative" -> config.creativeConfig.volume;
                case "minecraft:music.end" -> config.endConfig.volume;
                case "minecraft:music.under_water" -> config.underwaterConfig.volume;
                case "minecraft:music.credits" -> config.creditsConfig.volume;
                case "minecraft:music.game" -> config.gameConfig.volume;
                default -> {
                    if(sound.getId().toString().contains("overworld")) {
                        yield config.gameConfig.volume;
                    } else if (sound.getId().toString().contains("nether")) {
                        yield config.netherConfig.volume;
                    }
                    yield 100f;
                }
            };
            long note = config.mainConfig.varyPitch ? new Random().nextLong((config.mainConfig.maxNoteChange - config.mainConfig.minNoteChange) + 1) + config.mainConfig.minNoteChange : 0;
            cir.setReturnValue(new PositionedSoundInstance(sound.getId(), SoundCategory.MUSIC, volume / 100, (float) Math.pow(2.0D, (double) (note) / 12.0D), SoundInstance.createRandom(), false, 0, SoundInstance.AttenuationType.NONE, 0.0D, 0.0D, 0.0D, false));
        }
    }

    @Inject(method = "record", at = @At("RETURN"), cancellable = true)
    private static void recordMixin(SoundEvent sound, Vec3d pos, CallbackInfoReturnable<PositionedSoundInstance> cir) {
        if(config.mainConfig.enabled && config.jukeboxConfig.enabled) {
            if(config.jukeboxConfig.dogReplacesCat && sound.getId().toString().equals("minecraft:music_disc.cat")) {
                sound = SoundEvent.of(new Identifier("mixtape:music.dog"));
            } else if(config.jukeboxConfig.elevenReplaces11 && sound.getId().toString().equals("minecraft:music_disc.11")) {
                sound = SoundEvent.of(new Identifier("mixtape:music.eleven"));
            } else if(config.jukeboxConfig.droopyLikesYourFaceReplacesWard && sound.getId().toString().equals("minecraft:music_disc.ward")) {
                sound = SoundEvent.of(new Identifier("mixtape:music.droopy_likes_your_face"));
            }

            if(config.jukeboxConfig.mono) {
                cir.setReturnValue(new PositionedSoundInstance(sound.getId(), SoundCategory.RECORDS, config.jukeboxConfig.volume / 100, 1.0F, SoundInstance.createRandom(), false, 0, SoundInstance.AttenuationType.LINEAR, pos.x, pos.y, pos.z, true));
            } else {
                cir.setReturnValue(new PositionedSoundInstance(sound.getId(), SoundCategory.RECORDS, config.jukeboxConfig.volume / 100, 1.0F, SoundInstance.createRandom(), false, 0, SoundInstance.AttenuationType.LINEAR, pos.x, pos.y, pos.z, false));
            }
        }
    }
}
