package com.github.mouse0w0.wowforge.network;

import com.github.mouse0w0.wow.WowPlatform;
import com.github.mouse0w0.wow.network.NetworkManagerBase;
import com.github.mouse0w0.wow.network.Packet;
import com.github.mouse0w0.wow.network.packet.client.ClientVerificationPacket;
import com.github.mouse0w0.wow.network.packet.client.KeyBindingActionPacket;
import com.github.mouse0w0.wow.network.packet.server.RegisterKeyBindingPacket;
import com.github.mouse0w0.wow.network.packet.server.ServerVerificationPacket;
import com.github.mouse0w0.wowforge.WowForge;
import com.github.mouse0w0.wowforge.network.handler.RegisterKeyBindingPacketHandler;
import com.github.mouse0w0.wowforge.network.handler.ServerVerificationPacketHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

public class ForgeNetworkManager extends NetworkManagerBase {

    private FMLEventChannel channel;

    public void init() {
        channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(WowPlatform.getNetworkChannelName());
        channel.register(this);

        register(ServerVerificationPacket.class, new ServerVerificationPacketHandler()); // id 0;
        register(ClientVerificationPacket.class, null); // id 1;
        register(RegisterKeyBindingPacket.class, new RegisterKeyBindingPacketHandler()); // id 2;
        register(KeyBindingActionPacket.class, null); // id 3;
    }

    @Override
    protected void send(Object target, ByteBuf byteBuf) {
        FMLProxyPacket mcPacket = new FMLProxyPacket(new PacketBuffer(byteBuf), WowPlatform.getNetworkChannelName());
        channel.sendToServer(mcPacket);
    }

    @SubscribeEvent
    public void onClientPacket(FMLNetworkEvent.ClientCustomPacketEvent event) {
        ByteBuf buf = event.getPacket().payload();
        handle(null, buf.readBytes(buf.readableBytes()));
    }

    @SubscribeEvent
    public void onJoinServer(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        WowForge.resetRegistry();
    }
}
