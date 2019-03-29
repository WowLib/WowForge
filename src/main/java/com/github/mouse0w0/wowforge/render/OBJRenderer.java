package com.github.mouse0w0.wowforge.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.obj.OBJModel.Face;
import net.minecraftforge.client.model.obj.OBJModel.Group;
import net.minecraftforge.client.model.obj.OBJModel.Vertex;

@SuppressWarnings("deprecation")
public class OBJRenderer {

    /**
     * Render a given {@link OBJModel}
     * 
     * @param model
     *            The {@link OBJModel} that needs to render
     */
    public static void render(OBJModel model) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        for (Group group : model.getMatLib().getGroups().values()) {
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder vertexBuffer = tessellator.getBuffer();

            vertexBuffer.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_TEX_NORMAL);
            ResourceLocation resource = null;

            for (Face face : group.getFaces()) {
                String materialName = face.getMaterialName();
                if (!materialName.isEmpty()) {
                    String location = model.getMatLib().getMaterial(materialName).getTexture().getPath();
                    location += location.endsWith(".png") ? "" : ".png";
                    location = location.substring(0, location.indexOf(":")) + ":textures/" + location.substring(location.indexOf(":") + 1);
                    if (resource == null || !resource.getPath().equals(location))
                        Minecraft.getMinecraft().getTextureManager().bindTexture(resource = new ResourceLocation(location));
                }
                // Stich the texture

                Vertex[] vertices = face.getVertices();

                Vec3d first = new Vec3d(vertices[1].getPos().x - vertices[0].getPos().x, vertices[1].getPos().y - vertices[0].getPos().y, vertices[1].getPos().z - vertices[0].getPos().z);
                Vec3d second = new Vec3d(vertices[2].getPos().x - vertices[0].getPos().x, vertices[2].getPos().y - vertices[0].getPos().y, vertices[2].getPos().z - vertices[0].getPos().z);
                Vec3d normal = first.crossProduct(second).normalize();
                // Calculate normal

                for (int i = 0; i < 3; i++) {
                    Vertex vertex = vertices[i];
                    vertexBuffer.pos(vertex.getPos().x, vertex.getPos().y, vertex.getPos().z).tex(vertex.getTextureCoordinate().u, 1.0F - (vertex.getTextureCoordinate().v)).normal((float) normal.x, (float) normal.y, (float) normal.z).endVertex();
                    // Draw the face
                }
            }
            tessellator.draw();
        }

    }
}