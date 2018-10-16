package com.github.mouse0w0.wowforge;

import com.github.mouse0w0.wow.PlatformProvider;
import com.github.mouse0w0.wow.WowPlatform;
import com.github.mouse0w0.wow.network.NetworkManager;
import com.github.mouse0w0.wow.profile.Server;
import com.github.mouse0w0.wow.profile.User;
import com.github.mouse0w0.wow.registry.RegistryManager;
import com.github.mouse0w0.wow.registry.SimpleRegistryManager;
import com.github.mouse0w0.wowforge.keybinding.ClientKeyBindingManager;
import com.github.mouse0w0.wowforge.listener.InputListener;
import com.github.mouse0w0.wowforge.network.ForgeNetworkManager;
import com.github.mouse0w0.wowforge.network.handler.ServerVerificationPacketHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.UUID;

@Mod(modid = WowForge.NAME, acceptedMinecraftVersions = "[1.8,1.13)", clientSideOnly = true)
public class WowForge {

    public static final String NAME = "wowforge";

    private static final Logger logger = LogManager.getLogger(NAME);
    private static final User user = new ForgeUser();

    private static RegistryManager registryManager = new SimpleRegistryManager();
    private static ClientKeyBindingManager keyBindingManager = new ClientKeyBindingManager();

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
        InputListener.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        resetRegistry();
    }

    public static void resetRegistry() {
        getKeyBindingManager().clear();
    }

    public static NetworkManager getNetwork() {
        return network;
    }

    public static RegistryManager getRegistryManager() {
        return registryManager;
    }

    public static ClientKeyBindingManager getKeyBindingManager() {
        return keyBindingManager;
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

        @Override
        public RegistryManager getRegistryManager() {
            return WowForge.getRegistryManager();
        }

        @Override
        public boolean isServer() {
            return false;
        }

        @Override
        public Server getServer() {
            return ServerVerificationPacketHandler.getCurrentServer();
        }

        @Override
        public User getUser(UUID uuid) throws UnsupportedOperationException {
            throw new UnsupportedOperationException("This is client platform");
        }

        @Override
        public User getUser(String s) throws UnsupportedOperationException {
            throw new UnsupportedOperationException("This is client platform");
        }

        @Override
        public User getUser() throws UnsupportedOperationException {
            return WowForge.user;
        }
    }
}
