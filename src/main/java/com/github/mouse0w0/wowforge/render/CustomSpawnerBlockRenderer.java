package com.github.mouse0w0.wowforge.render;

import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntityMobSpawnerRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;

public class CustomSpawnerBlockRenderer extends TileEntitySpecialRenderer<TileEntityMobSpawner> {
    public static final PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 15);
    public static final PropertyDirection FACING = PropertyDirection.create("facing");
    public static TileEntityMobSpawnerRenderer original = new TileEntityMobSpawnerRenderer();

    @Override
    public void render(TileEntityMobSpawner spawner, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        Entity entity = spawner.getSpawnerBaseLogic().getCachedEntity();
        if (entity == null || entity.getCustomNameTag() == null || entity.getCustomNameTag().isEmpty() || !entity.getCustomNameTag().startsWith("render:")) {
            try {
                original.setRendererDispatcher(rendererDispatcher);
                original.render(spawner, x, y, z, partialTicks, destroyStage, alpha);
                // redirect to original renderer if it is not a wow model.
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return;
        }
        if (!(spawner.getWorld().getBlockState(spawner.getPos()) instanceof CustomState)) {
            spawner.getWorld().setBlockState(spawner.getPos(), new CustomState(spawner.getWorld().getBlockState(spawner.getPos())));
        }
        String nameTag = entity.getCustomNameTag().substring("render:".length());
        String modelLocation = "";
        if (nameTag.startsWith("model:")) {
            modelLocation = "wow:models/block/" + nameTag.substring("model:".length()) + ".obj";
        } else if (nameTag.startsWith("json:")) {
            modelLocation = "wow:models/block/" + nameTag.substring("json:".length());
        }

        if (modelLocation.isEmpty()) {
            return;
        }

        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();

        GlStateManager.translate(x, y, z);
        GlStateManager.translate(0.5, 0, 0.5);
        GlStateManager.scale(4, 4, 4);
        GlStateManager.rotate(270, 0, 1, 0);
        try {
            RenderHelper.enableStandardItemLighting();
            int i = spawner.getWorld().getCombinedLight(spawner.getPos(), 0);
            int j = i % 65536;
            int k = i / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            if (nameTag.startsWith("json:")) {
                GlStateManager.translate(-spawner.getPos().getX(), -spawner.getPos().getY(), -spawner.getPos().getZ());
                JsonRenderer.render(spawner.getWorld(), spawner.getPos(), new ResourceLocation(modelLocation));
            } else {
                OBJRenderer.render((OBJModel) OBJLoader.INSTANCE.loadModel(new ResourceLocation(modelLocation)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        GlStateManager.popMatrix();
        GlStateManager.popAttrib();

    }

}
