package com.github.mouse0w0.wowforge;

import com.github.mouse0w0.wow.WowPlatform;
import com.github.mouse0w0.wow.profile.User;
import net.minecraft.client.Minecraft;

import java.util.UUID;

public class ForgeUser implements User {

    private final UUID userUuid = UUID.fromString(Minecraft.getMinecraft().getSession().getPlayerID());

    @Override
    public boolean isSupport() {
        return true;
    }

    @Override
    public int getVersion() {
        return WowPlatform.getInternalVersion();
    }

    @Override
    public UUID getUUID() {
        return userUuid;
    }

    @Override
    public String getName() {
        return Minecraft.getMinecraft().getSession().getUsername();
    }

    @Override
    public Object getSource() {
        return Minecraft.getMinecraft().getSession();
    }
}
