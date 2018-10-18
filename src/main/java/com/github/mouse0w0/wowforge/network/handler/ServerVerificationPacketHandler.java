package com.github.mouse0w0.wowforge.network.handler;

import com.github.mouse0w0.wow.WowPlatform;
import com.github.mouse0w0.wow.network.PacketHandler;
import com.github.mouse0w0.wow.network.packet.client.ClientVerificationPacket;
import com.github.mouse0w0.wow.network.packet.server.ServerVerificationPacket;
import com.github.mouse0w0.wow.profile.Server;
import com.github.mouse0w0.wowforge.WowForge;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import javax.annotation.Nonnull;

public class ServerVerificationPacketHandler implements PacketHandler<ServerVerificationPacket> {

    private static Server currentServer = Server.UNSUPPORTED_SERVER;

    public ServerVerificationPacketHandler() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Nonnull
    public static Server getCurrentServer() {
        return currentServer;
    }

    @Override
    public void handle(Object sender, ServerVerificationPacket packet) {
        currentServer = new Server(true, packet.getInternalVersion(), packet.getUUID(), Minecraft.getMinecraft().getCurrentServerData().serverIP);
        WowForge.getLogger().info("Received server verification. Version: " + currentServer.getVersion());
        WowForge.getNetwork().send(null, new ClientVerificationPacket(WowPlatform.getInternalVersion()));
    }

    @SubscribeEvent
    public void onLeaveServer(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        currentServer = Server.UNSUPPORTED_SERVER;
        WowForge.getLogger().info("Disconnected from server.");
    }
}
