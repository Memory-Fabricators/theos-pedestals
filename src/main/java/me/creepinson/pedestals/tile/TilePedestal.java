package me.creepinson.pedestals.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.UUID;

public class TilePedestal extends TileEntity {

    public double speed = 2;
    private ItemStackHandler handler = new ItemStackHandler(1);
    private boolean locked;
    public boolean hovering = true;
    protected UUID owner;

    public ItemStack getStack() {
        return handler.getStackInSlot(0);
    }

    public void setStack(ItemStack stack) {
        this.handler.setStackInSlot(0, stack);
        markDirty();
        if (world != null) {
            IBlockState state = world.getBlockState(getPos());
            world.notifyBlockUpdate(getPos(), state, state, 3);
        }
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        // getUpdateTag() is called whenever the chunkdata is sent to the
        // client. In contrast getUpdatePacket() is called when the tile entity
        // itself wants to sync to the client. In many cases you want to send
        // over the same information in getUpdateTag() as in getUpdatePacket().
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        // Prepare a packet for syncing our TE to the client. Since we only have to sync the stack
        // and that's all we have we just write our entire NBT here. If you have a complex
        // tile entity that doesn't need to have all information on the client you can write
        // a more optimal NBT here.
        NBTTagCompound nbtTag = new NBTTagCompound();
        this.writeToNBT(nbtTag);
        return new SPacketUpdateTileEntity(getPos(), 1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        // Here we get the packet from the server and read it into our client side tile entity
        this.readFromNBT(packet.getNbtCompound());
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("item")) {
            setStack(new ItemStack(compound.getCompoundTag("item")));
        } else {
            setStack(ItemStack.EMPTY);
        }
        this.locked = compound.getBoolean("locked");
        this.owner = compound.getUniqueId("owner");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (!isEmpty()) {
            NBTTagCompound tagCompound = new NBTTagCompound();
            handler.getStackInSlot(0).writeToNBT(tagCompound);
            compound.setTag("item", tagCompound);
        }

        compound.setBoolean("locked", this.locked);
        compound.setUniqueId("owner", this.owner);

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
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T) handler : null;
    }
}