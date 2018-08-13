package com.github.mouse0w0.wowforge;

import com.github.mouse0w0.wow.network.NetworkManager;
import com.github.mouse0w0.wowforge.network.ForgeNetworkManager;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = WowForge.NAME, acceptedMinecraftVersions = "[1.8,1.13)", clientSideOnly = true)
public class WowForge {

    public static final String NAME = "wowforge";

    private static final Logger logger = LogManager.getLogger(NAME);

    @Mod.Instance(NAME)
    public static WowForge instance;

    private static ForgeNetworkManager network;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        network = new ForgeNetworkManager();
        network.init();
    }

    public static NetworkManager getNetwork() {
        return network;
    }

    public static Logger getLogger() {
        return logger;
    }
}
