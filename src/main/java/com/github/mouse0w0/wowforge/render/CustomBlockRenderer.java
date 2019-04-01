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
    public void render(TileEntitySign sign, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (!((sign.signText[0].getFormattedText()).startsWith("[[[render:") && sign.signText[2].getFormattedText().startsWith("]]]"))) {
            try {
                original.setRendererDispatcher(rendererDispatcher);
                original.render(sign, x, y, z, partialTicks, destroyStage, alpha);
                // redirect to original renderer if it is not a wow model.
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return;
        }

        if (!(sign.getWorld().getBlockState(sign.getPos()) instanceof CustomState)) {
            sign.getWorld().setBlockState(sign.getPos(), new CustomState(sign.getWorld().getBlockState(sign.getPos())));
        }

        String line = sign.signText[1].getUnformattedText();
        String modelLocation = "";

        boolean isJson = false;
        if (line.startsWith("model:")) {
            modelLocation = "wow:models/block/" + line.substring("model:".length()) + ".obj";
        } else if (line.startsWith("json:")) {
            isJson = true;
            modelLocation = "wow:models/block/" + line.substring("json:".length());

        }
        if (modelLocation.isEmpty()) {
            return;
        }
        IBlockState state = Minecraft.getMinecraft().world.getBlockState(sign.getPos());

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
        int i = sign.getWorld().getCombinedLight(sign.getPos(), 0);
        int j = i % 65536;
        int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        try {
            if (isJson) {
                GlStateManager.translate(-sign.getPos().getX(), -sign.getPos().getY(), -sign.getPos().getZ());
                JsonRenderer.render(sign.getWorld(), sign.getPos(), new ResourceLocation(modelLocation));
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
