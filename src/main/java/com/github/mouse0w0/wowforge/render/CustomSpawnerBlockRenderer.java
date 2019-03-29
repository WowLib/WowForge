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
    public void render(TileEntityMobSpawner tspawn, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        Entity entity = tspawn.getSpawnerBaseLogic().getCachedEntity();
        if (entity == null || entity.getCustomNameTag() == null || entity.getCustomNameTag().isEmpty() || !entity.getCustomNameTag().startsWith("render:")) {
            try {
                original.setRendererDispatcher(rendererDispatcher);
                original.render(tspawn, x, y, z, partialTicks, destroyStage, alpha);
                // redirect to original renderer if it is not a wow model.
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return;
        }
        if (!(tspawn.getWorld().getBlockState(tspawn.getPos()) instanceof CustomState)) {
            tspawn.getWorld().setBlockState(tspawn.getPos(), new CustomState(tspawn.getWorld().getBlockState(tspawn.getPos())));
        }
        String nameTag = entity.getCustomNameTag().substring("render:".length());
        String modelLocation = "";
        if (nameTag.startsWith("model:")) {
            modelLocation = "wow:models/block/" + nameTag.substring("model:".length()) + ".obj";
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
            int i = tspawn.getWorld().getCombinedLight(tspawn.getPos(), 0);
            int j = i % 65536;
            int k = i / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            OBJRenderer.render((OBJModel) OBJLoader.INSTANCE.loadModel(new ResourceLocation(modelLocation)));

        } catch (Exception e) {
            e.printStackTrace();
        }

        GlStateManager.popMatrix();
        GlStateManager.popAttrib();

    }

}
