package me.creepinson.pedestals.block;

import me.creepinson.creepinoutils.base.BaseBlockWithTile;
import me.creepinson.creepinoutils.util.TextComponentGroup;
import me.creepinson.pedestals.PedestalsMod;
import me.creepinson.pedestals.PedestalsRegistryHandler;
import me.creepinson.pedestals.tile.PedestalTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class PedestalBlock extends BaseBlockWithTile {

    public PedestalBlock() {
        super(new ResourceLocation(PedestalsMod.MOD_ID, "pedestal"), Block.Properties.create(Material.IRON)
                .hardnessAndResistance(2.5F, 2F).harvestLevel(2).harvestTool(ToolType.PICKAXE).variableOpacity());
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return PedestalsRegistryHandler.PEDESTAL_TILE.get().create();
    }

    @Override
    public void harvestBlock(World world, PlayerEntity player, BlockPos pos, BlockState state,
                             @Nullable TileEntity tile, ItemStack stack) {
        super.harvestBlock(world, player, pos, state, tile, stack);

        if (tile instanceof PedestalTile) {
            tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(h -> {
                ItemStackHandler itemHandler = (ItemStackHandler) h;
                for (int i = 0; i < itemHandler.getSlots(); i++) {
                    if (!itemHandler.getStackInSlot(i).isEmpty()) {
                        world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(),
                                itemHandler.getStackInSlot(i)));
                    }
                }
            });
        }
        world.setBlockState(pos, Blocks.AIR.getDefaultState());
    }

    @Override
    public boolean isNormalCube(BlockState state, IBlockReader world, BlockPos pos) {
        return false;
    }

    private PedestalTile getTE(World world, BlockPos pos) {
        return (PedestalTile) world.getTileEntity(pos);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player,
                                             Hand hand, BlockRayTraceResult hit) {
        PedestalTile te = getTE(world, pos);
        // this will not perform anything on the client
        if (!world.isRemote) {
            if (player.isSneaking() && te.getOwner().equals(player.getUniqueID())) {
                player.sendMessage(
                        new TextComponentGroup(TextFormatting.DARK_RED).string("[Pedestals] ", TextFormatting.AQUA)
                                .string("This pedestal has been ").string((te.isLocked() ? "unlocked!" : "locked!")));
                te.setLocked(!te.isLocked());
            } else {
                if (te.isLocked()) {
                    player.sendMessage(new TextComponentGroup(TextFormatting.RED)
                            .string("[Pedestals] ", TextFormatting.AQUA).string("This pedestal is locked!"));
                } else {
                    if (!player.getHeldItem(hand).isEmpty()) {
                        ItemStack previous = te.getStack().copy();
                        te.setStack(player.getHeldItem(hand));
                        player.inventory.setInventorySlotContents(player.inventory.currentItem, previous);

                    } else if (!te.getStack().isEmpty()) {
                        if (te.isLocked()) {
                            player.sendMessage(new TextComponentGroup(TextFormatting.RED)
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
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);
        if (world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof PedestalTile
                && !world.getTileEntity(pos).isRemoved()) {
            PedestalTile tile = getTE(world, pos);
            tile.setOwner(placer.getUniqueID());
        }
    }
}