package info.u_team.useful_railroads.container;

import info.u_team.u_team_core.container.UContainer;
import info.u_team.useful_railroads.init.UsefulRailroadsContainerTypes;
import info.u_team.useful_railroads.inventory.*;
import info.u_team.useful_railroads.util.TrackBuilderMode;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.container.*;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IntReferenceHolder;

public class TrackBuilderContainer extends UContainer {
	
	private final TrackBuilderInventoryWrapper wrapper;
	
	// Client
	public TrackBuilderContainer(int id, PlayerInventory playerInventory, PacketBuffer buffer) {
		this(id, playerInventory, new TrackBuilderInventoryWrapper.Client(buffer.readVarInt(), buffer.readEnumValue(TrackBuilderMode.class), () -> playerInventory.player.world));
	}
	
	// Server
	public TrackBuilderContainer(int id, PlayerInventory playerInventory, TrackBuilderInventoryWrapper wrapper) {
		super(UsefulRailroadsContainerTypes.TRACK_BUILDER, id);
		this.wrapper = wrapper;
		appendInventory(wrapper.getFuelInventory(), FuelItemSlotHandler::new, 1, 1, 152, 132);
		appendInventory(wrapper.getRailInventory(), 1, 9, 8, 32);
		appendInventory(wrapper.getGroundBlockInventory(), 3, 9, 8, 64);
		appendInventory(wrapper.getRedstoneTorchInventory(), 1, 2, 8, 132);
		appendPlayerInventory(playerInventory, 8, 164);
		trackInt(new IntReferenceHolder() {
			
			@Override
			public int get() {
				return wrapper.getFuel();
			}
			
			@Override
			public void set(int fuel) {
				wrapper.setFuel(fuel);
			}
			
		});
		trackInt(new IntReferenceHolder() {
			
			@Override
			public int get() {
				return wrapper.getMode().ordinal();
			}
			
			@Override
			public void set(int ordinal) {
				wrapper.setMode(TrackBuilderMode.class.getEnumConstants()[ordinal]);
			}
		});
	}
	
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		wrapper.writeItemStack();
	}
	
	@Override
	public ItemStack transferStackInSlot(PlayerEntity player, int index) {
		ItemStack remainingStack = ItemStack.EMPTY;
		final Slot slot = inventorySlots.get(index);
		
		if (slot != null && slot.getHasStack()) {
			final ItemStack stack = slot.getStack();
			remainingStack = stack.copy();
			
			if (index < 39) {
				if (!mergeItemStack(stack, 39, inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else {
				if (index >= 66) {
					if (!mergeItemStack(stack, 0, 66, false)) {
						return ItemStack.EMPTY;
					}
				} else {
					if (!mergeItemStack(stack, 0, 39, false)) {
						if (!mergeItemStack(stack, 66, 75, false)) {
							return ItemStack.EMPTY;
						}
					}
				}
			}
			
			if (stack.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
		}
		return remainingStack;
	}
	
	@Override
	public ItemStack slotClick(int index, int dragType, ClickType clickType, PlayerEntity player) {
		Slot tmpSlot;
		if (index >= 0 && index < inventorySlots.size()) {
			tmpSlot = inventorySlots.get(index);
		} else {
			tmpSlot = null;
		}
		if (tmpSlot != null) {
			if (tmpSlot.inventory == player.inventory && tmpSlot.getSlotIndex() == player.inventory.currentItem) {
				return tmpSlot.getStack();
			}
		}
		if (clickType == ClickType.SWAP) {
			ItemStack stack = player.inventory.getStackInSlot(dragType);
			if (stack == player.inventory.getCurrentItem()) {
				return ItemStack.EMPTY;
			}
		}
		return super.slotClick(index, dragType, clickType, player);
	}
	
	public TrackBuilderInventoryWrapper getWrapper() {
		return wrapper;
	}
	
}
