package com.theoparis.forge;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class PedestalBlock extends Block implements EntityBlock {
  private static final VoxelShape SHAPE =
      Shapes.or(
          Block.box(6, 2, 6, 10, 14, 10),
          Block.box(4, 0, 4, 12, 1, 12),
          Block.box(5, 1, 5, 11, 2, 11),
          Block.box(7, 14, 12, 9, 15, 13),
          Block.box(3, 14, 7, 4, 15, 9),
          Block.box(7, 14, 3, 9, 15, 4),
          Block.box(3, 15, 4, 4, 16, 12),
          Block.box(12, 15, 4, 13, 16, 12),
          Block.box(12, 14, 7, 13, 15, 9),
          Block.box(4, 15, 3, 12, 16, 4),
          Block.box(4, 15, 12, 12, 16, 13),
          Block.box(4, 14, 4, 12, 16, 12));

  public PedestalBlock(Identifier registryName) {
    super(
        BlockBehaviour.Properties.of()
            .setId(ResourceKey.create(Registries.BLOCK, registryName))
            .mapColor(MapColor.METAL)
            .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
            .requiresCorrectToolForDrops()
            .sound(SoundType.METAL)
            .strength(2.5F, 2F)
            .sound(SoundType.WOOD)
            .noOcclusion());
  }

  @Override
  protected @NonNull VoxelShape getShape(
      @NonNull BlockState state,
      @NonNull BlockGetter level,
      @NonNull BlockPos pos,
      @NonNull CollisionContext context) {
    return SHAPE;
  }

  @Override
  protected boolean propagatesSkylightDown(@NonNull BlockState state) {
    return true;
  }

  @Override
  protected @NonNull RenderShape getRenderShape(@NonNull BlockState state) {
    return RenderShape.MODEL;
  }

  //
  //	@Override
  //	public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState,
  // boolean isMoving) {
  //		getTE(world, pos).ifPresent(tile -> {
  //			// drops everything in the inventory
  //			tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
  //				for (int i = 0; i < h.getSlots(); i++) {
  //					spawnAsEntity(world, pos, h.getStackInSlot(i));
  //				}
  //			});
  //			world.removeTileEntity(pos);
  //		});
  //	}

  //	@Override
  //	public boolean isNormalCube(BlockState state, IBlockReader world, BlockPos pos) {
  //		return false;
  //	}
  //
  //	public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
  //		return true;
  //	}
  //
  //	@Override
  //	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos,
  // PlayerEntity player,
  //	                                         Hand hand, BlockRayTraceResult hit) {
  //		TileEntity tileEntity = world.getTileEntity(pos);
  //		// this will not perform anything on the client
  //		return getTE(world, pos).map(te -> {
  //			if (!world.isRemote) {
  //				if (player.isSneaking() && te.getOwner().equals(player.getUniqueID())) {
  //					player.sendMessage(
  //						new GroupTextComponent(TextFormatting.DARK_RED).string("[Pedestals] ",
  // TextFormatting.AQUA)
  //							.string("This pedestal has been ").string((te.isLocked() ? "unlocked!" : "locked!")));
  //					te.setLocked(!te.isLocked());
  //				} else {
  //					if (te.isLocked()) {
  //						player.sendMessage(new GroupTextComponent(TextFormatting.RED)
  //							.string("[Pedestals] ", TextFormatting.AQUA).string("This pedestal is locked!"));
  //					} else {
  //						if (!player.getHeldItem(hand).isEmpty()) {
  //							ItemStack previous = te.getStack().copy();
  //							te.setStack(player.getHeldItem(hand));
  //							player.inventory.setInventorySlotContents(player.inventory.currentItem, previous);
  //
  //						} else if (!te.getStack().isEmpty()) {
  //							if (te.isLocked()) {
  //								player.sendMessage(new GroupTextComponent(TextFormatting.RED)
  //									.string("[Pedestals] ", TextFormatting.AQUA).string("This pedestal is locked!"));
  //							} else {
  //								// There is a stack in the pedestal.
  //								// In this case we remove itand try to put it in the // players inventory if
  //								// there is room ItemStack
  //								ItemStack stack = te.getStack();
  //
  //								if (!player.inventory.addItemStackToInventory(stack)) {
  //									// Not possible. Throw item in the world
  //									world.addEntity(new ItemEntity(world, pos.getX(), pos.getY() + 1, pos.getZ(), stack));
  //								}
  //								te.setStack(ItemStack.EMPTY);
  //							}
  //						}
  //					}
  //				}
  //			}
  //			return ActionResultType.SUCCESS;
  //		}).orElse(ActionResultType.PASS);
  //	}
  //
  //	@Override
  //	public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer,
  // ItemStack stack) {
  //		super.onBlockPlacedBy(world, pos, state, placer, stack);
  //		getTE(world, pos).ifPresent(tile -> tile.setOwner(placer.getUniqueID()));
  //	}
  //
  //	@Override
  //	public int getStrongPower(BlockState state, IBlockReader world, BlockPos pos, Direction side) {
  //		return 0;
  //	}
  //
  //	@Override
  //	public BlockRenderType getRenderType(BlockState state) {
  //		return BlockRenderType.MODEL;
  //	}
  //
  //	@Override
  //	public int getWeakPower(BlockState state, IBlockReader world, BlockPos pos, Direction side) {
  //		return getStrongPower(state, world, pos, side);
  //	}
  //
  //	@Override
  //	public int getComparatorInputOverride(BlockState state, World world, BlockPos pos) {
  //		return getTE(world, pos).map(PedestalTile::getOutputLevel).orElse(0);
  //	}
  //
  //	@Override
  //	public boolean hasComparatorInputOverride(BlockState state) {
  //		return true;
  //	}

  @Override
  public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return null;
  }
}
