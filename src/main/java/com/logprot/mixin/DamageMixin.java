package com.logprot.mixin;

import com.logprot.Logprot;
import com.logprot.players.PlayerManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public class DamageMixin
{
    @Inject(at = @At("HEAD"), method = "hurtServer", cancellable = true)
    private void onDamage(final ServerLevel level, final DamageSource source, final float damage, final CallbackInfoReturnable<Boolean> cir)
    {
        if (PlayerManager.getInstance().isPlayerImmune((Player) (Object) this, source))
        {
            cir.setReturnValue(false);
        }
    }

    @Inject(at = @At(value = "HEAD"), method = "triggerDimensionChangeTriggers")
    private void onChangeDim(final ServerLevel oldLevel, final CallbackInfo ci)
    {
        if (Logprot.config.getCommonConfig().dimensionprotection)
        {
            PlayerManager.getInstance().onPlayerLogin((ServerPlayer) (Object) this);
        }
    }

    @Inject(at = @At(value = "RETURN"), method = "restoreFrom")
    private void onRespawn(
        final ServerPlayer serverPlayer,
        final boolean bl,
        final CallbackInfo ci)
    {
        if (Logprot.config.getCommonConfig().dimensionprotection)
        {
            PlayerManager.getInstance().onPlayerLogin((ServerPlayer) (Object) this);
        }
    }
}
