package me.creepinson.pedestals;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

/**
 * @author Creepinson http://gitlab.com/creepinson
 **/
@Mod(PedestalsMod.MOD_ID)
@Mod.EventBusSubscriber
public class PedestalsMod {

    public static final String MOD_ID = "pedestals";
    private static final String VERSION = "1.0.0";

    public PedestalsMod() {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the items, blocks, etc. for the mod
        PedestalsRegistryHandler.init();
    }
}
