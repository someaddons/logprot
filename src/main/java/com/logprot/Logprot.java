package com.logprot;

import com.cupboard.config.CupboardConfig;
import com.logprot.config.CommonConfiguration;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Constants.MOD_ID)
public class Logprot
{
    public static final Logger LOGGER = LogManager.getLogger();

    /**
     * The config instance.
     */
    public static CupboardConfig<CommonConfiguration> config = new CupboardConfig<>(Constants.MOD_ID, new CommonConfiguration());

    public Logprot(IEventBus modEventBus, ModContainer modContainer)
    {

        modEventBus.addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("Shields up!");
    }
}
