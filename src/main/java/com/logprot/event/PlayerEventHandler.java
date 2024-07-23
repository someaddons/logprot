package com.logprot.event;

import com.logprot.players.PlayerManager;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

/**
 * Eventhandler for the players which are currently invulnerable, removed when no players are invulnverable.
 */
public class PlayerEventHandler
{
    private static PlayerEventHandler instance = new PlayerEventHandler();

    public static PlayerEventHandler getInstance()
    {
        return instance;
    }

    @SubscribeEvent
    public void onServerTick(final ServerTickEvent.Post event)
    {
        PlayerManager.getInstance().updatePlayers();
    }

    @SubscribeEvent
    public void onLivingDamageEvent(LivingDamageEvent.Pre event)
    {
        if (!(event.getEntity() instanceof Player))
        {
            return;
        }

        if (PlayerManager.getInstance().isPlayerImmune((Player) event.getEntity(), event.getSource()))
        {
            event.setNewDamage(0);
        }
    }
}
