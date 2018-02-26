package com.darkona.adventurebackpack.block;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import com.darkona.adventurebackpack.inventory.IInventoryTanks;

/**
 * Created on 26.02.2018
 *
 * @author Ugachaga
 */
abstract class TileAdventure extends TileEntity implements IInventoryTanks
{
    /** IInventory START --- */

    @Override
    public int getSizeInventory()
    {
        return getInventory().length;
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
        return getInventory()[slot];
    }

    @Nullable
    @Override
    public ItemStack decrStackSize(int slot, int quantity)
    {
        ItemStack stack = getStackInSlot(slot);
        if (stack != null)
        {
            if (stack.stackSize <= quantity)
                setInventorySlotContents(slot, null);
            else
                stack = stack.splitStack(quantity);
        }
        markDirty();
        return stack;
    }

    @Nullable
    @Override
    public ItemStack getStackInSlotOnClosing(int slot)
    {
        for (int s : getSlotsOnClosingArray())
            if (slot == s)
                return getInventory()[slot];

        return null;
    }

    @Override
    public void setInventorySlotContents(int slot, @Nullable ItemStack stack)
    {
        setInventorySlotContentsNoSave(slot, stack);
        markDirty();
    }

    @Override
    public String getInventoryName()
    {
        return "";
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return getInventoryName() != null && !getInventoryName().isEmpty();
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    // we have to inherit markDirty() implemented in TileEntity.class

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this
                && player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) <= 64;
    }

    @Override
    public void openInventory()
    {
        //
    }

    @Override
    public void closeInventory()
    {
        markDirty();
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack)
    {
        return false; // override when automation is allowed
    }

    /* --- IInventory END || IAsynchronousInventory START --- */

    @Nullable
    @Override
    public ItemStack decrStackSizeNoSave(int slot, int quantity)
    {
        ItemStack stack = getStackInSlot(slot);
        if (stack != null)
        {
            if (stack.stackSize <= quantity)
                setInventorySlotContentsNoSave(slot, null);
            else
                stack = stack.splitStack(quantity);
        }
        return stack;
    }

    @Override
    public void setInventorySlotContentsNoSave(int slot, @Nullable ItemStack stack)
    {
        if (slot >= getSizeInventory())
            return;

        if (stack != null)
        {
            if (stack.stackSize > getInventoryStackLimit())
                stack.stackSize = getInventoryStackLimit();

            if (stack.stackSize == 0)
                stack = null;
        }

        getInventory()[slot] = stack;
    }

    /* --- IAsynchronousInventory END || IInventoryTanks START --- */


}
