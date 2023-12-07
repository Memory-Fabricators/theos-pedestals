package com.theoparis

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.minecraft.client.render.RenderLayer

object PedestalsModClient : ClientModInitializer {
  override fun onInitializeClient() {
    BlockRenderLayerMap.INSTANCE.putBlock(PedestalsMod.PEDESTAL_BLOCK, RenderLayer.getCutout())
  }
}
