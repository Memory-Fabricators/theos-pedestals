package com.theoparis

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroups
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import org.slf4j.LoggerFactory

object PedestalsMod : ModInitializer {
	val MOD_ID = "pedestals"
	val logger = LoggerFactory.getLogger(MOD_ID)

	val PEDESTAL_BLOCK =
		registerBlock(
			"pedestal",
			Block(
				AbstractBlock.Settings.copy(Blocks.CRAFTING_TABLE).nonOpaque().registryKey(
					RegistryKey.of(
						RegistryKeys.BLOCK,
						Identifier.of(MOD_ID, "pedestal"),
					),
				),
			),
		)

	override fun onInitialize() {
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register { entries ->
			entries.add(PEDESTAL_BLOCK)
		}
	}

	private fun registerItem(
		name: String,
		item: Item,
	): Item = Registry.register(Registries.ITEM, Identifier.of(MOD_ID, name), item)

	private fun registerBlock(
		name: String,
		block: Block,
	): Block {
		registerBlockItem(name, block)
		return Registry.register(Registries.BLOCK, Identifier.of(MOD_ID, name), block)
	}

	private fun registerBlockItem(
		name: String,
		block: Block,
	): Item =
		registerItem(
			name,
			BlockItem(
				block,
				Item.Settings().registryKey(
					RegistryKey.of(
						RegistryKeys.ITEM,
						Identifier.of(MOD_ID, "pedestal"),
					),
				),
			),
		)
}
