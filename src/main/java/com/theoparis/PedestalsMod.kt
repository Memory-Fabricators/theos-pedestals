package com.theoparis

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroups
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import org.slf4j.LoggerFactory

object PedestalsMod : ModInitializer {
  val MOD_ID = "pedestals"
  val logger = LoggerFactory.getLogger(MOD_ID)

  val PEDESTAL_BLOCK =
      registerBlock(
          "pedestal",
          Block(FabricBlockSettings.copyOf(Blocks.CRAFTING_TABLE).nonOpaque())
      )

  override fun onInitialize() {
    ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register { entries ->
      entries.add(PEDESTAL_BLOCK)
    }
  }

  private fun registerItem(name: String, item: Item): Item {
    return Registry.register(Registries.ITEM, Identifier(MOD_ID, name), item)
  }

  private fun registerBlock(name: String, block: Block): Block {
    registerBlockItem(name, block)
    return Registry.register(Registries.BLOCK, Identifier(MOD_ID, name), block)
  }

  private fun registerBlockItem(name: String, block: Block): Item {
    return registerItem(name, BlockItem(block, FabricItemSettings()))
  }
}
