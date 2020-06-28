package me.creepinson.pedestals.tile.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import me.creepinson.pedestals.tile.PedestalTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

/**
 * @author Creepinson http://gitlab.com/creepinson
 **/
public class PedestalTileRenderer extends TileEntityRenderer<PedestalTile> {
    public PedestalTileRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(PedestalTile te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
        matrixStack.push();

        // Render item on top of pedestal
        ItemStack stack = te.getStack();
        float angle = (System.currentTimeMillis() / 40) % 360;
        double hoverHeight = te.hovering ? MathHelper.sin(((float) te.getAge() + partialTicks) / 10.0F) * 0.1F + 0.1F : 0;
//        double hoverHeight = 0.5F;
        if (!stack.isEmpty()) {
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableLighting();

            // Translate to the center of the block and directly above the pedestal
//            matrixStack.translate(te.getPos().getX() + 0.5, te.getPos().getY() + 1.35 + hoverHeight, te.getPos().getZ() + 0.5);
            matrixStack.translate(0.5, 1.25 + hoverHeight, 0.5);

            if (te.hovering)
                matrixStack.rotate(Vector3f.YP.rotationDegrees(angle)); //reverting the rotation with an equal but negative angle

            matrixStack.scale(.6f, .6f, .6f);

            Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.FIXED, combinedLightIn, combinedOverlayIn, matrixStack, buffer);
        }

        matrixStack.pop();
    }
}