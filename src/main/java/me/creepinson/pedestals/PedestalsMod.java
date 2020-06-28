package me.creepinson.pedestals;

import me.creepinson.pedestals.tile.render.PedestalTileRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * @author Creepinson http://gitlab.com/creepinson
 **/
@Mod(PedestalsMod.MOD_ID)
@Mod.EventBusSubscriber
public class PedestalsMod {

    public static final String MOD_ID = "pedestals";

    public PedestalsMod() {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the items, blocks, etc. for the mod
        PedestalsRegistryHandler.init();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

    }

    private void clientSetup(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(PedestalsRegistryHandler.PEDESTAL_BLOCK.get(), RenderType.getCutout());
        ClientRegistry.bindTileEntityRenderer(PedestalsRegistryHandler.PEDESTAL_TILE.get(), PedestalTileRenderer::new);
    }
}
