package com.aliahx.mixtape.mixin;

import com.aliahx.mixtape.Mixtape;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.inventory.SingleStackInventory;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(JukeboxBlockEntity.class)
public abstract class JukeboxBlockEntityMixin implements SingleStackInventory {

    @Shadow private long tickCount;

    @Shadow private long recordStartTick;

    @Inject(method = "tick(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/entity/JukeboxBlockEntity;)V", at = @At("HEAD"))
    private static void tickMixin(World world, BlockPos pos, BlockState state, JukeboxBlockEntity blockEntity, CallbackInfo ci) {
        if(Mixtape.lastJukeBoxes.containsKey(pos)) {
            if(!Mixtape.lastLastJukeBoxes.get(pos) && !Mixtape.lastJukeBoxes.get(pos) && !blockEntity.isPlayingRecord()) {
                Mixtape.jukeBoxesPlaying.put(pos, false);
            } else {
                Mixtape.jukeBoxesPlaying.put(pos, true);
            }
        } else {
            Mixtape.jukeBoxesPlaying.put(pos, true);
        }

        Mixtape.lastLastJukeBoxes.put(pos, Mixtape.lastJukeBoxes.getOrDefault(pos, false));
        Mixtape.lastJukeBoxes.put(pos, blockEntity.isPlayingRecord());
    }

    @Inject(method = "isSongFinished", at = @At("HEAD"))
    private void isSongFinishedMixin(MusicDiscItem musicDisc, CallbackInfoReturnable<Boolean> cir) {
        if(tickCount >= this.recordStartTick + (long)musicDisc.getSongLengthInTicks() + 20L) {
            BlockPos pos = ((JukeboxBlockEntity)(Object)this).getPos();
            Mixtape.jukeBoxesPlaying.remove(pos);
            Mixtape.lastJukeBoxes.remove(pos);
            Mixtape.lastLastJukeBoxes.remove(pos);
        }
    }
}
