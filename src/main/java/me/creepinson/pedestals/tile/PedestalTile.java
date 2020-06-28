package me.creepinson.pedestals.tile;

import me.creepinson.pedestals.PedestalsRegistryHandler;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.UUID;

public class PedestalTile extends TileEntity implements ITickableTileEntity {
    public double speed = 2;
    private LazyOptional<ItemStackHandler> lazyHandler;
    private ItemStackHandler handler = new ItemStackHandler(1);
    private boolean locked;
    public boolean hovering = true;
    protected UUID owner;
    private int age;

    public PedestalTile() {
        super(PedestalsRegistryHandler.PEDESTAL_TILE.get());
        this.lazyHandler = LazyOptional.of(() -> handler);
    }

    public ItemStack getStack() {
        return handler.getStackInSlot(0);
    }

    public void setStack(ItemStack stack) {
        this.handler.setStackInSlot(0, stack);
        markDirty();
        if (world != null) {
            BlockState state = world.getBlockState(getPos());
            world.updateComparatorOutputLevel(pos, state.getBlock());
            world.notifyBlockUpdate(pos, state, state, 3);
        }
    }

    @Override
    public CompoundNBT getUpdateTag() {
        // getUpdateTag() is called whenever the chunkdata is sent to the
        // client. In contrast getUpdatePacket() is called when the tile entity
        // itself wants to sync to the client. In many cases you want to send
        // over the same information in getUpdateTag() as in getUpdatePacket().
        return write(new CompoundNBT());
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        // Prepare a packet for syncing our TE to the client. Since we only have to sync
        // the stack
        // and that's all we have we just write our entire NBT here. If you have a
        // complex
        // tile entity that doesn't need to have all information on the client you can
        // write
        // a more optimal NBT here.
        CompoundNBT nbtTag = new CompoundNBT();
        this.write(nbtTag);
        return new SUpdateTileEntityPacket(getPos(), 1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        // Here we get the packet from the server and read it into our client side tile
        // entity
        this.read(packet.getNbtCompound());
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        if (compound.contains("item")) {
            setStack(ItemStack.read(compound.getCompound("item")));
        } else {
            setStack(ItemStack.EMPTY);
        }
        this.locked = compound.getBoolean("locked");
        this.owner = compound.getUniqueId("owner");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        if (!isEmpty()) {
            CompoundNBT tagCompound = new CompoundNBT();
            handler.getStackInSlot(0).write(tagCompound);
            compound.put("item", tagCompound);
        }

        compound.putBoolean("locked", this.locked);
        compound.putUniqueId("owner", this.owner);

        return compound;
    }

    public boolean isEmpty() {
        return handler.getStackInSlot(0) == ItemStack.EMPTY;
    }

    public boolean isLocked() {
        return this.locked;
    }

    public void setLocked(boolean b) {
        this.locked = b;
    }

    public void setOwner(UUID uniqueID) {
        this.owner = uniqueID;
    }

    public UUID getOwner() {
        return this.owner;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (LazyOptional<T>) lazyHandler
                : LazyOptional.empty();
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    public int getOutputLevel() {
        if (!this.isEmpty()) return this.getStack().getCount();
        else return 0;
    }

    public int getAge() {
        return this.age;
    }

    @Override
    public void tick() {
        if (!this.removed) this.age++;
    }
}