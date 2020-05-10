package me.creepinson.pedestals;

import me.creepinson.pedestals.block.BlockPedestal;
import me.creepinson.pedestals.tile.TilePedestal;
import me.creepinson.pedestals.tile.render.TileRendererPedestal;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Creepinson http://gitlab.com/creepinson
 **/
@Mod.EventBusSubscriber
public class RegistryHandler {

    public static BlockPedestal pedestal = new BlockPedestal("pedestal");

    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(pedestal);
        GameRegistry.registerTileEntity(TilePedestal.class, pedestal.getRegistryName());
    }

    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(pedestal.createItemBlock());
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerModels(final ModelRegistryEvent event) {
        registerItemRenderer(Item.getItemFromBlock(pedestal), 0, pedestal.getRegistryName());
        ClientRegistry.bindTileEntitySpecialRenderer(TilePedestal.class,
                new TileRendererPedestal());
    }

    private static void registerItemRenderer(Item item, int meta, ResourceLocation id) {
        ModelLoader.setCustomModelResourceLocation(item, meta,
                new ModelResourceLocation(id, "inventory"));
    }
}
