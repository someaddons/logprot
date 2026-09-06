package com.logprot.mixin;

import com.logprot.players.PlayerManager;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public class PlayerListMixin
{
    @Inject(method = "placeNewPlayer", at = @At("TAIL"))
    private void onPlayerLogin(final Connection connection, final ServerPlayer player, final CommonListenerCookie cookie, final CallbackInfo ci)
    {
        PlayerManager.getInstance().onPlayerLogin(player);
    }
}
