package com.github.mouse0w0.wowforge.network;

import com.github.mouse0w0.wow.WowPlatform;
import com.github.mouse0w0.wow.network.NetworkManagerBase;
import com.github.mouse0w0.wow.network.Packet;
import com.github.mouse0w0.wow.network.packet.client.ClientVerificationPacket;
import com.github.mouse0w0.wow.network.packet.server.ServerVerificationPacket;
import com.github.mouse0w0.wowforge.WowForge;
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
        channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(WowPlatform.NAME);
        channel.register(this);

        register(ServerVerificationPacket.class, new ServerVerificationPacketHandler());
        register(ClientVerificationPacket.class, null);
    }

    @Override
    public void send(Object target, Packet packet) {
        ByteBuf buf = createBuffer(packet.getClass());
        packet.write(buf);
        FMLProxyPacket mcPacket = new FMLProxyPacket(new PacketBuffer(buf), WowPlatform.NAME);
        channel.sendToServer(mcPacket);
    }

    @SubscribeEvent
    public void onClientPacket(FMLNetworkEvent.ClientCustomPacketEvent event) {
        ByteBuf buf = event.getPacket().payload();
        handle(null, buf.readBytes(buf.readableBytes()));
    }
}
