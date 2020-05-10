package me.creepinson.pedestals.tile.render;

import me.creepinson.pedestals.tile.TilePedestal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Creepinson http://gitlab.com/creepinson
 **/
@SideOnly(Side.CLIENT)
public class TileRendererPedestal extends TileEntitySpecialRenderer<TilePedestal> {

    @Override
    public void render(TilePedestal te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();

        // Translate to the location of our tile entity
        GlStateManager.translate(x, y, z);
        GlStateManager.disableRescaleNormal();

        // Render our item
        renderItem(te);

        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }

    private void renderItem(TilePedestal te) {
        ItemStack stack = te.getStack();
        float angle = (System.currentTimeMillis() / 40) % 360;
        double hoverHeight = te.hovering ? MathHelper.sin((float) ((te.speed) / 10.0F)) * 0.1F + 0.1F : 0;
        if (!stack.isEmpty()) {
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableLighting();
            GlStateManager.pushMatrix();

            // Translate to the center of the block and directly above the pedestal
            GlStateManager.translate(.5, 1.35 + hoverHeight, .5);

            if (te.hovering)
                GlStateManager.rotate(angle, 0, 1, 0); //reverting the rotation with an equal but negative angle

            GlStateManager.scale(.4f, .4f, .4f);

            Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);

            GlStateManager.popMatrix();
        }
    }

}