package com.github.mouse0w0.wowforge.render;

import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;

public class JsonModelLoader {
    public static void load(ResourceLocation... resources) {
        ModelBakery.registerItemVariants(Items.BAKED_POTATO, resources);
        for (ResourceLocation location : resources) {
            modelCache.put(location.getPath(), ModelLoaderRegistry.getModelOrMissing(location));
        }
    }

    public static IModel get(ResourceLocation location) {
        if (!modelCache.containsKey(location.getPath()))
            modelCache.put(location.getPath(), ModelLoaderRegistry.getModelOrMissing(location));
        return modelCache.get(location.getPath());
    }

    private static HashMap<String, IModel> modelCache = new HashMap<>();
    private static HashMap<String, IBakedModel> bakedModelCache = new HashMap<>();

    public static IBakedModel bake(ResourceLocation location) {
        if (!bakedModelCache.containsKey(location.getPath())) {
            bakedModelCache.put(location.getPath(), get(location).bake(TRSRTransformation.identity(), DefaultVertexFormats.BLOCK, locations -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(locations.toString())));
        }
        return bakedModelCache.get(location.getPath());
    }
}
