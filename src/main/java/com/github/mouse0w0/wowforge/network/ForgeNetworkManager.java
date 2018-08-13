package com.github.mouse0w0.wowforge.network;

import com.github.mouse0w0.wow.WowPlatform;
import com.github.mouse0w0.wow.network.NetworkManagerBase;
import com.github.mouse0w0.wow.network.Packet;
import com.github.mouse0w0.wow.network.packet.common.VerificationPacket;
import com.github.mouse0w0.wowforge.WowForge;
import com.github.mouse0w0.wowforge.network.handler.VerificationPacketHandler;
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

    public ForgeNetworkManager() {
        register(VerificationPacket.class, new VerificationPacketHandler());
    }

    public void init() {
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
        channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(WowPlatform.NAME);
        channel.register(WowForge.instance);
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
