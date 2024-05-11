package com.logprot.players;

import com.logprot.Logprot;
import com.logprot.Utils.BlockPosUtils;
import com.logprot.event.PlayerEventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

/**
 * Class managing the players which logged in
 */
public class PlayerManager
{
    /**
     * Singleton instance
     */
    private static PlayerManager instance;

    /**
     * Stores the logged players, allows gc deletion
     */
    private Map<UUID, PlayerData> playerDataMap = new HashMap<>();

    private PlayerManager() {}

    /**
     * Get the instance
     *
     * @return
     */
    public static PlayerManager getInstance()
    {
        if (PlayerManager.instance == null)
        {
            PlayerManager.instance = new PlayerManager();
            Mod.EventBusSubscriber.Bus.FORGE.bus().get().register(PlayerEventHandler.getInstance());
        }
        return PlayerManager.instance;
    }

    /**
     * Add a player on login
     *
     * @param player
     */
    public void onPlayerLogin(final Player player)
    {
        if (playerDataMap.containsKey(player.getUUID()))
        {
            return;
        }
        playerDataMap.put(player.getUUID(), new PlayerData(player, player.blockPosition(), System.currentTimeMillis() +  (Logprot.config.getCommonConfig().invulTime/20) * 1000L));
        if (Logprot.config.getCommonConfig().debugOutput)
        {
            Logprot.LOGGER.info("Player:" + player.getDisplayName().getString() + " now has protection for " + Logprot.config.getCommonConfig().invulTime + " ticks");
        }
    }

    /**
     * Checks the players current distance with the starting position, and removes invuln if needed.
     */
    public void updatePlayers()
    {
        if (playerDataMap.isEmpty())
        {
            return;
        }

        Iterator<Map.Entry<UUID, PlayerData>> iterator = playerDataMap.entrySet().iterator();

        long currentTime = System.currentTimeMillis();

        while (iterator.hasNext())
        {
            Map.Entry<UUID, PlayerData> entry = iterator.next();

            if (!entry.getValue().player.isAlive())
            {
                iterator.remove();
                break;
            }

            // Use timepoints instead?
            if (entry.getValue().invulTimePoint <= currentTime)
            {
                if (Logprot.config.getCommonConfig().debugOutput)
                {
                    Logprot.LOGGER.info("Player:" + entry.getValue().player.getName().getString() + " got his login protection removed due to timeout");
                }

                entry.getValue().player.hurtTime = 0;
                iterator.remove();
            }
        }
    }

    /**
     * Whether the player is immune
     *
     * @param playerEntity
     * @param source
     * @return
     */
    public boolean isPlayerImmune(final Player playerEntity, final DamageSource source)
    {
        if (source == playerEntity.damageSources().fall())
        {
            return false;
        }

        updatePlayers();
        return playerDataMap.containsKey(playerEntity.getUUID());
    }

    public void onPlayerTeleport(final ServerPlayer player)
    {
        playerDataMap.put(player.getUUID(),
          new PlayerData(player, new BlockPos(player.getBlockX(), player.getBlockY(), player.getBlockZ()), Logprot.config.getCommonConfig().invulTime));
        if (Logprot.config.getCommonConfig().debugOutput)
        {
            Logprot.LOGGER.info("Teleported player:" + player.getName().getString() + " now has login protection for " + Logprot.config.getCommonConfig().invulTime + " ticks");
        }
    }

    /**
     * Removes the player protection
     * @param player
     */
    public void removeProtection(final ServerPlayer player)
    {
        if (player != null && playerDataMap.containsKey(player.getUUID()) && playerDataMap.remove(player.getUUID()) != null && Logprot.config.getCommonConfig().debugOutput)
        {
            Logprot.LOGGER.info("Player:" + player.getName().getString() + " got his login protection removed due to activity");
        }
    }
}
