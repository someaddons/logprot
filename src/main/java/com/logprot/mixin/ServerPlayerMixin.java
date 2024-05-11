package com.logprot.mixin;

import com.logprot.players.PlayerManager;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin
{
    @Inject(method = "resetLastActionTime", at = @At("HEAD"))
    private void onAction(final CallbackInfo ci)
    {
        PlayerManager.getInstance().removeProtection((ServerPlayer)(Object) this);
    }
}
