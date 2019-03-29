package com.github.mouse0w0.wowforge.render;

import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySignRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;

public class CustomBlockRenderer extends TileEntitySpecialRenderer<TileEntitySign> {
    public static final PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 15);
    public static final PropertyDirection FACING = PropertyDirection.create("facing");
    public static TileEntitySignRenderer original = new TileEntitySignRenderer();

    @Override
    public void render(TileEntitySign te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (!((te.signText[0].getFormattedText()).startsWith("[[[render:") && te.signText[2].getFormattedText().startsWith("]]]"))) {
            try {
                original.setRendererDispatcher(rendererDispatcher);
                original.render(te, x, y, z, partialTicks, destroyStage, alpha);
                // redirect to original renderer if it is not a wow model.
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return;
        }

        if (!(te.getWorld().getBlockState(te.getPos()) instanceof CustomState)) {
            te.getWorld().setBlockState(te.getPos(), new CustomState(te.getWorld().getBlockState(te.getPos())));
        }
        String line = te.signText[1].getUnformattedText();
        String modelLocation = "";
        if (line.startsWith("model:")) {
            modelLocation = "wow:models/block/" + line.substring("model:".length()) + ".obj";
        }
        if (modelLocation.isEmpty()) {
            return;
        }
        IBlockState state = Minecraft.getMinecraft().world.getBlockState(te.getPos());

        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();

        GlStateManager.translate(x, y, z);
        GlStateManager.translate(0.5, 0, 0.5);
        GlStateManager.scale(4, 4, 4);

        if (state.getPropertyKeys().contains(FACING)) {
            GlStateManager.rotate((float) (state.getValue(FACING).getHorizontalAngle()) + 180, 0, 1, 0);
        } else if (state.getPropertyKeys().contains(ROTATION))
            GlStateManager.rotate((float) (state.getValue(ROTATION).intValue() * 22.5) + 180, 0, 1, 0);

        RenderHelper.enableStandardItemLighting();
        int i = te.getWorld().getCombinedLight(te.getPos(), 0);
        int j = i % 65536;
        int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        try {
            OBJRenderer.render((OBJModel) OBJLoader.INSTANCE.loadModel(new ResourceLocation(modelLocation)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        GlStateManager.popMatrix();
        GlStateManager.popAttrib();

    }

}
