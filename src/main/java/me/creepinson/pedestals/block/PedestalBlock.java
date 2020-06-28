package me.creepinson.pedestals.block;

import me.creepinson.creepinoutils.base.BaseBlockWithTile;
import me.creepinson.creepinoutils.util.BlockUtils;
import me.creepinson.creepinoutils.util.text.GroupTextComponent;
import me.creepinson.pedestals.PedestalsRegistryHandler;
import me.creepinson.pedestals.tile.PedestalTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

public class PedestalBlock extends BaseBlockWithTile {

    public PedestalBlock() {
        super(Block.Properties.create(Material.IRON)
                .hardnessAndResistance(2.5F, 2F).harvestLevel(1).harvestTool(ToolType.PICKAXE).sound(SoundType.WOOD).variableOpacity().notSolid());
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return PedestalsRegistryHandler.PEDESTAL_TILE.get().create();
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        getTE(world, pos).ifPresent(tile -> {
            // drops everything in the inventory
            tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                for (int i = 0; i < h.getSlots(); i++) {
                    spawnAsEntity(world, pos, h.getStackInSlot(i));
                }
            });
            world.removeTileEntity(pos);
        });
    }

    private static LazyOptional<PedestalTile> getTE(IWorld world, BlockPos pos) {
        return BlockUtils.getTile(world, pos).filter(tile -> tile instanceof PedestalTile).map(tile -> (PedestalTile) tile);
    }

    @Override
    public boolean isNormalCube(BlockState state, IBlockReader world, BlockPos pos) {
        return false;
    }

    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return true;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player,
                                             Hand hand, BlockRayTraceResult hit) {
        TileEntity tileEntity = world.getTileEntity(pos);
        // this will not perform anything on the client
        return getTE(world, pos).map(te -> {
            if (!world.isRemote) {
                if (player.isSneaking() && te.getOwner().equals(player.getUniqueID())) {
                    player.sendMessage(
                            new GroupTextComponent(TextFormatting.DARK_RED).string("[Pedestals] ", TextFormatting.AQUA)
                                    .string("This pedestal has been ").string((te.isLocked() ? "unlocked!" : "locked!")));
                    te.setLocked(!te.isLocked());
                } else {
                    if (te.isLocked()) {
                        player.sendMessage(new GroupTextComponent(TextFormatting.RED)
                                .string("[Pedestals] ", TextFormatting.AQUA).string("This pedestal is locked!"));
                    } else {
                        if (!player.getHeldItem(hand).isEmpty()) {
                            ItemStack previous = te.getStack().copy();
                            te.setStack(player.getHeldItem(hand));
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, previous);

                        } else if (!te.getStack().isEmpty()) {
                            if (te.isLocked()) {
                                player.sendMessage(new GroupTextComponent(TextFormatting.RED)
                                        .string("[Pedestals] ", TextFormatting.AQUA).string("This pedestal is locked!"));
                            } else {
                                // There is a stack in the pedestal.
                                // In this case we remove itand try to put it in the // players inventory if
                                // there is room ItemStack
                                ItemStack stack = te.getStack();

                                if (!player.inventory.addItemStackToInventory(stack)) {
                                    // Not possible. Throw item in the world
                                    world.addEntity(new ItemEntity(world, pos.getX(), pos.getY() + 1, pos.getZ(), stack));
                                }
                                te.setStack(ItemStack.EMPTY);
                            }
                        }
                    }
                }
            }
            return ActionResultType.SUCCESS;
        }).orElse(ActionResultType.PASS);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);
        getTE(world, pos).ifPresent(tile -> tile.setOwner(placer.getUniqueID()));
    }

    @Override
    public int getStrongPower(BlockState state, IBlockReader world, BlockPos pos, Direction side) {
        return 0;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public int getWeakPower(BlockState state, IBlockReader world, BlockPos pos, Direction side) {
        return getStrongPower(state, world, pos, side);
    }

    @Override
    public int getComparatorInputOverride(BlockState state, World world, BlockPos pos) {
        return getTE(world, pos).map(PedestalTile::getOutputLevel).orElse(0);
    }

    @Override
    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }
}