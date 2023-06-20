package com.aliahx.mixtape.mixin;

import com.google.common.collect.Multimap;
import net.minecraft.SharedConstants;
import net.minecraft.client.sound.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static com.aliahx.mixtape.Mixtape.client;
import static com.aliahx.mixtape.Mixtape.config;

@Mixin(SoundSystem.class)
public abstract class SoundSystemMixin {
    @Shadow @Final private Map<SoundInstance, Channel.SourceManager> sources;

    @Shadow protected abstract float getAdjustedVolume(SoundInstance sound);

    @Shadow private boolean started;

    @Shadow @Final private SoundListener listener;

    @Shadow @Final private SoundManager loader;

    @Shadow @Final private static Set<Identifier> UNKNOWN_SOUNDS;

    @Shadow @Final private static Logger LOGGER;

    @Shadow @Final private static Marker MARKER;

    @Shadow protected abstract float getAdjustedPitch(SoundInstance sound);

    @Shadow @Final private List<SoundInstanceListener> listeners;

    @Shadow
    protected static boolean shouldRepeatInstantly(SoundInstance sound) {
        return false;
    }

    @Shadow @Final private Channel channel;

    @Shadow @Final private Map<SoundInstance, Integer> soundEndTicks;

    @Shadow private int ticks;

    @Shadow @Final private Multimap<SoundCategory, SoundInstance> sounds;

    @Shadow @Final private SoundLoader soundLoader;

    @Shadow @Final private List<TickableSoundInstance> tickingSounds;

    @Inject(at = @At("HEAD"), method = "play(Lnet/minecraft/client/sound/SoundInstance;)V", cancellable = true)
    private void play(SoundInstance sound, CallbackInfo ci) {
        if(sound.getCategory() == SoundCategory.RECORDS && config.jukeboxConfig.mono) {
            if (this.started) {
                if (sound.canPlay()) {

                    WeightedSoundSet weightedSoundSet = sound.getSoundSet(this.loader);
                    Identifier identifier = sound.getId();
                    if (weightedSoundSet == null) {
                        if (UNKNOWN_SOUNDS.add(identifier)) {
                            LOGGER.warn((Marker) MARKER, (String) "Unable to play unknown soundEvent: {}", (Object) identifier);
                        }

                    } else {
                        Sound sound2 = sound.getSound();
                        if (sound2 != SoundManager.INTENTIONALLY_EMPTY_SOUND) {
                            if (sound2 == SoundManager.MISSING_SOUND) {
                                if (UNKNOWN_SOUNDS.add(identifier)) {
                                    LOGGER.warn((Marker) MARKER, (String) "Unable to play empty soundEvent: {}", (Object) identifier);
                                }

                            } else {
                                float f = sound.getVolume();
                                float g = Math.max(f, 1.0F) * (float) sound2.getAttenuation();
                                SoundCategory soundCategory = sound.getCategory();
                                float h = this.getAdjustedVolume(sound);
                                float i = this.getAdjustedPitch(sound);
                                SoundInstance.AttenuationType attenuationType = sound.getAttenuationType();
                                boolean bl = sound.isRelative();
                                if (h == 0.0F && !sound.shouldAlwaysPlay()) {
                                    LOGGER.debug((Marker) MARKER, (String) "Skipped playing sound {}, volume was zero.", (Object) sound2.getIdentifier());
                                } else {
                                    Vec3d vec3d = new Vec3d(sound.getX(), sound.getY(), sound.getZ());
                                    boolean bl2;
                                    if (!this.listeners.isEmpty()) {
                                        bl2 = bl || attenuationType == SoundInstance.AttenuationType.NONE || this.listener.getPos().squaredDistanceTo(vec3d) < (double) (g * g);
                                        if (bl2) {
                                            Iterator var14 = this.listeners.iterator();

                                            while (var14.hasNext()) {
                                                SoundInstanceListener soundInstanceListener = (SoundInstanceListener) var14.next();
                                                soundInstanceListener.onSoundPlayed(sound, weightedSoundSet);
                                            }
                                        } else {
                                            LOGGER.debug((Marker) MARKER, (String) "Did not notify listeners of soundEvent: {}, it is too far away to hear", (Object) identifier);
                                        }
                                    }

                                    if (this.listener.getVolume() <= 0.0F) {
                                        LOGGER.debug((Marker) MARKER, (String) "Skipped playing soundEvent: {}, master volume was zero", (Object) identifier);
                                    } else {
                                        bl2 = shouldRepeatInstantly(sound);
                                        boolean bl3 = sound2.isStreamed();
                                        CompletableFuture<Channel.SourceManager> completableFuture = this.channel.createSource(sound2.isStreamed() ? SoundEngine.RunMode.STREAMING : SoundEngine.RunMode.STATIC);
                                        Channel.SourceManager sourceManager = (Channel.SourceManager) completableFuture.join();
                                        if (sourceManager == null) {
                                            if (SharedConstants.isDevelopment) {
                                                LOGGER.warn("Failed to create new sound handle");
                                            }

                                        } else {
                                            LOGGER.debug(MARKER, "Playing sound {} for event {}", sound2.getIdentifier(), identifier);
                                            this.soundEndTicks.put(sound, this.ticks + 20);
                                            this.sources.put(sound, sourceManager);
                                            this.sounds.put(soundCategory, sound);
                                            boolean finalBl = bl2;
                                            sourceManager.run((source) -> {
                                                source.setPitch(i);
                                                source.setVolume(h);
                                                if (attenuationType == SoundInstance.AttenuationType.LINEAR) {
                                                    source.setAttenuation(g);
                                                } else {
                                                    source.disableAttenuation();
                                                }

                                                source.setLooping(finalBl && !bl3);
                                                source.setPosition(vec3d);
                                                if(bl) {
                                                    source.setPosition(new Vec3d(0,0,0));
                                                }
                                                source.setRelative(bl);
                                            });
                                            if (!bl3) {
                                                this.soundLoader.loadStatic(sound2.getLocation()).thenAccept((soundx) -> {
                                                    sourceManager.run((source) -> {
                                                        source.setBuffer(soundx);
                                                        source.play();
                                                    });
                                                });
                                            } else {
                                                this.soundLoader.loadStreamed(sound2.getLocation(), bl2).thenAccept((stream) -> {
                                                    sourceManager.run((source) -> {
                                                        source.setStream(stream);
                                                        source.play();
                                                    });
                                                });
                                            }

                                            if (sound instanceof TickableSoundInstance) {
                                                this.tickingSounds.add((TickableSoundInstance) sound);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            ci.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "updateSoundVolume", cancellable = true)
    private void updateSoundVolume(SoundCategory category, float volume, CallbackInfo ci) {
        this.sources.forEach((source, sourceManager) -> {
            if(source.getCategory() == SoundCategory.RECORDS) {
                double distanceToPlayer = Math.sqrt(listener.getPos().squaredDistanceTo(source.getX(), source.getY(), source.getZ()));
                sourceManager.run((sourcex) -> {
                    float newVolume = (float) (config.jukeboxConfig.distance - distanceToPlayer) / config.jukeboxConfig.distance;
                    sourcex.setVolume(newVolume < 0 ? 0 : newVolume);
                });
            }
        });

        if (this.started && category == SoundCategory.MUSIC) {
                this.sources.forEach((source, sourceManager) -> {
                    if(source.getCategory() == SoundCategory.MUSIC) {
                        float f = this.getAdjustedVolume(source) * volume;
                        sourceManager.run((sourcex) -> {
                            sourcex.setVolume(f);
                        });
                    }
                });
            ci.cancel();
        }
    }
}