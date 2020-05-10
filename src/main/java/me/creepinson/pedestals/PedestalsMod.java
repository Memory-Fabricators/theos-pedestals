package me.creepinson.pedestals;

import me.creepinson.creepinoutils.base.BaseMod;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * @author Creepinson http://gitlab.com/creepinson
 **/
@Mod(modid = PedestalsMod.MOD_ID, dependencies = "after:enderio")
@Mod.EventBusSubscriber
public class PedestalsMod extends BaseMod {

    public static final String MOD_ID = "pedestals";
    private static final String VERSION = "1.0.0";

    @Mod.Instance(PedestalsMod.MOD_ID)
    public static PedestalsMod instance;

    public PedestalsMod() {
        super("", null, MOD_ID, VERSION);
    }

    public static PedestalsMod getInstance() {
        return instance;
    }

    @Override
    public void clientPreInit(FMLPreInitializationEvent event) {
        super.clientPreInit(event);
    }
}
