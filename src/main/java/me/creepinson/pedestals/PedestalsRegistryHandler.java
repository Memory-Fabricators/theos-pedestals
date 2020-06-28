package me.creepinson.pedestals;

import me.creepinson.pedestals.block.PedestalBlock;
import me.creepinson.pedestals.tile.PedestalTile;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author Creepinson http://gitlab.com/creepinson
 **/
public class PedestalsRegistryHandler {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
            PedestalsMod.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
            PedestalsMod.MOD_ID);
    private static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister
            .create(ForgeRegistries.TILE_ENTITIES, PedestalsMod.MOD_ID);
    private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister
            .create(ForgeRegistries.CONTAINERS, PedestalsMod.MOD_ID);
    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES,
            PedestalsMod.MOD_ID);
    private static final DeferredRegister<ModDimension> DIMENSIONS = DeferredRegister
            .create(ForgeRegistries.MOD_DIMENSIONS, PedestalsMod.MOD_ID);
    private static ItemGroup ITEM_GROUP;

    // Register things here
    public static final RegistryObject<PedestalBlock> PEDESTAL_BLOCK = BLOCKS.register("pedestal", () -> new PedestalBlock());

    public static final RegistryObject<Item> PEDESTAL_BLOCK_ITEM = ITEMS.register("pedestal",
            () -> new BlockItem(PEDESTAL_BLOCK.get(), new Item.Properties().group(ITEM_GROUP)));
    public static final RegistryObject<TileEntityType<PedestalTile>> PEDESTAL_TILE = TILES.register("pedestal",
            () -> TileEntityType.Builder.create(PedestalTile::new, PEDESTAL_BLOCK.get()).build(null));

    public static void init() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        DIMENSIONS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEM_GROUP = new ItemGroup(PedestalsMod.MOD_ID) {
            @Override
            public ItemStack createIcon() {
                // Custom creative tab icon here
                // You can use the items that you created earlier in this class as well.
                return new ItemStack(Items.APPLE);
            }

        };
    }

    public static ItemGroup getItemGroup() {
        return ITEM_GROUP;
    }
}