package com.github.mouse0w0.wowforge;

import com.github.mouse0w0.wow.PlatformProvider;
import com.github.mouse0w0.wow.WowPlatform;
import com.github.mouse0w0.wow.network.NetworkManager;
import com.github.mouse0w0.wow.profile.Server;
import com.github.mouse0w0.wowforge.listener.KeyInputListener;
import com.github.mouse0w0.wowforge.network.ForgeNetworkManager;
import com.github.mouse0w0.wowforge.network.handler.ServerVerificationPacketHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

@Mod(modid = WowForge.NAME, acceptedMinecraftVersions = "[1.8,1.13)", clientSideOnly = true)
public class WowForge {

    public static final String NAME = "wowforge";

    private static final Logger logger = LogManager.getLogger(NAME);

    @Mod.Instance(NAME)
    public static WowForge instance;

    private static ForgeNetworkManager network;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        getLogger().info("Initializing Wow. Version: " + WowPlatform.getVersion() + " . Internal version: " + WowPlatform.getInternalVersion());
        network = new ForgeNetworkManager();
        network.init();

        WowPlatform.setPlatformProvider(new ForgePlatformProvider());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        KeyInputListener.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }

    public static NetworkManager getNetwork() {
        return network;
    }

    public static Logger getLogger() {
        return logger;
    }

    @Nonnull
    public static Server getCurrentServer() {
        return ServerVerificationPacketHandler.getCurrentServer();
    }

    public static class ForgePlatformProvider implements PlatformProvider {

        @Override
        public NetworkManager getNetwork() {
            return WowForge.getNetwork();
        }
    }
}
