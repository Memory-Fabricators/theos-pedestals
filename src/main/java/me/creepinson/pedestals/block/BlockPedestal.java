package me.creepinson.pedestals.block;

import me.creepinson.creepinoutils.base.BaseBlockWithTile;
import me.creepinson.pedestals.PedestalsMod;
import me.creepinson.pedestals.tile.TilePedestal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

@Optional.Interface(iface = "crazypants.enderio.base.block.painted.IPaintable.ITexturePaintableBlock", modid = "enderio")
public class BlockPedestal extends BaseBlockWithTile {

    public BlockPedestal(String name) {
        super(Material.IRON, new ResourceLocation(PedestalsMod.getInstance().modId, name), CreativeTabs.DECORATIONS, 2.5F, 2F, 2, "pickaxe");
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        return super.removedByPlayer(state, world, pos, player, willHarvest);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity tile = world.getTileEntity(pos);

        super.breakBlock(world, pos, state);
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity tile, ItemStack stack) {
        super.harvestBlock(world, player, pos, state, tile, stack);

        if (tile instanceof TilePedestal) {
            ItemStackHandler itemHandler = (ItemStackHandler) tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                if (!itemHandler.getStackInSlot(i).isEmpty()) {
                    world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), itemHandler.getStackInSlot(i)));
                }
            }
        }
        world.setBlockToAir(pos);
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        TileEntity tile = world.getTileEntity(pos);

        super.getDrops(drops, world, pos, state, fortune);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public boolean isBlockNormalCube(IBlockState blockState) {
        return
                false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
    }

    private TilePedestal getTE(World world, BlockPos pos) {
        return
                (TilePedestal) world.getTileEntity(pos);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos,
                                    IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing,
                                    float hitX, float hitY, float hitZ) {
        TilePedestal te = getTE(world, pos);
        // this will not perform anything on the client
        if (!world.isRemote) {
            if (player.isSneaking() && te.getOwner().equals(player.getUniqueID())) {
                player.sendMessage(new
                        TextComponentTranslation(TextFormatting.AQUA + "[Pedestals] Security > " +
                        TextFormatting.DARK_RED + "This pedestal has been " + (te.isLocked() ? "unlocked!" : "locked!")));
                te.setLocked(!te.isLocked());
            } else {
                if (te.isLocked()) {
                    player.sendMessage(new TextComponentTranslation(TextFormatting.AQUA +
                            "[Pedestals] " + TextFormatting.RED + "This pedestal is locked!"));
                } else {
                    if (!player.getHeldItem(hand).isEmpty()) {
                        ItemStack previous = te.getStack().copy();
                        te.setStack(player.getHeldItem(hand));
                        player.inventory.setInventorySlotContents(player.inventory.currentItem,
                                previous);

                    } else if (!te.getStack().isEmpty()) {
                        if (te.isLocked()) {
                            player.sendMessage(new TextComponentTranslation(TextFormatting.AQUA +
                                    "[Pedestals] " + TextFormatting.RED + "This pedestal is locked!"));
                        } else {
                            // There is a stack in the pedestal.
                            // In this case we remove itand try to put it in the // players inventory if there is room ItemStack
                            ItemStack stack = te.getStack();

                            if (!player.inventory.addItemStackToInventory(stack)) {
                                // Not possible. Throw item in the world
                                world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY() + 1, pos.getZ(), stack));
                            }
                            te.setStack(ItemStack.EMPTY);
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);
        if (world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TilePedestal && !world.getTileEntity(pos).isInvalid()) {
            TilePedestal tile = getTE(world, pos);
            tile.setOwner(placer.getUniqueID());
        }
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TilePedestal();
    }
}