package com.theoparis.forge;

import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(PedestalsModForge.MOD_ID)
public class PedestalsModForge {
  public static final String MOD_ID = "pedestals";
  public static final DeferredRegister<Block> BLOCKS =
      DeferredRegister.createBlocks(PedestalsModForge.MOD_ID);
  public static final DeferredHolder<Block, Block> PEDESTAL_BLOCK =
      BLOCKS.register("pedestal", PedestalBlock::new);

  public PedestalsModForge(IEventBus modBus) {
    BLOCKS.register(modBus);
  }

  @SubscribeEvent
  public void onInit(FMLCommonSetupEvent event) {}
}
